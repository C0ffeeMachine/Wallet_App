package com.example.WalletService.Controller;

import com.example.WalletService.Model.AddBalanceDetails;
import com.example.WalletService.Model.Transaction;
import com.example.WalletService.Model.User;
import com.example.WalletService.Model.Wallet;
import com.example.WalletService.Repository.TransactionRepository;
import com.example.WalletService.Repository.WalletRepository;
import com.example.WalletService.Util.TransactionValidator;
import com.example.WalletService.Util.WalletValidator;
import com.example.WalletService.exception.TransactionBadRequest;
import com.example.WalletService.exception.WalletNotFoundException;
import com.example.WalletService.service.EmailService;
import com.example.WalletService.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@RestController
public class TransactionResource {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserService userService;

    TransactionValidator transactionValidator = new TransactionValidator();
    WalletValidator walletValidator = new WalletValidator();
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionResource.class);

    @PostMapping(value = "/sendMoney")
    @ResponseStatus(HttpStatus.CREATED)
    Transaction addBal(@RequestBody Transaction transaction) throws Exception {
        if(!transactionValidator.validateTransaction(transaction)){
            throw new TransactionBadRequest();
        }
        transaction.setDate(new Date(Calendar.getInstance().getTime().getTime()));
        User sender = userService.getAUser(String.valueOf(transaction.getSid()));
        User receiver = userService.getAUser(String.valueOf(transaction.getRid()));
        if(sender==null || receiver==null){
            LOGGER.info("No wallet for sender or receiver");
            throw new TransactionBadRequest();
        }

        Wallet senderWallet = walletRepository.findWalletByUserId(sender.getId());
        Wallet receiverWallet = walletRepository.findWalletByUserId(receiver.getId());

        int amt = transaction.getAmount();

        if(senderWallet.getBalance()<amt){
            throw new Exception("Not enough balance");
        }

        senderWallet.setBalance(senderWallet.getBalance()-amt);
        receiverWallet.setBalance(receiverWallet.getBalance()+amt);

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        transaction.setStatus("SUCCESS");
        return transactionRepository.save(transaction);
    }

    @GetMapping(value = "getBal/{id}")
    int getBal(@PathVariable int id){
        Wallet wallet = walletRepository.findWalletByUserId(id);
        if(wallet == null){
            throw new WalletNotFoundException(id);
        }
        return wallet.getBalance();
    }

    @PostMapping(value = "/addBalance")
    AddBalanceDetails addBalance(@RequestBody AddBalanceDetails request){
        LOGGER.info(request.toString());
        Wallet wallet = walletRepository.findWalletByUserId(request.getUid());

        if(wallet==null){
            throw new WalletNotFoundException(request.getUid());
        }
        wallet.setBalance(request.getBalance()+wallet.getBalance());
        walletRepository.save(wallet);
        return request;
    }

    @GetMapping(value = "/txnHistory/{id}")
    String getTxnHistory(@PathVariable int id){
        LOGGER.info(String.format("$$ -> Producing Transaction --> %s", id));
        sendTxnHistory(String.valueOf(id));
        return "Check your email for the file.";
    }

    private void sendTxnHistory(String id) {
        int id1 = Integer.parseInt(id);
        ArrayList<Transaction> list = (ArrayList<Transaction>) transactionRepository.findBysidAndrid(id1);

        User user = userService.getAUser(String.valueOf(id1));
        String filename = user.getName() + "_" + "transactions" + ".csv";
        try {
            FileWriter fw = new FileWriter(filename);
            for (int i = 0; i < list.size(); i++) {
                fw.append(list.get(i).getStatus());
                fw.append(',');
                int amt = list.get(i).getAmount();
                Integer obj = amt;
                fw.append(obj.toString());
                fw.append(',');
                fw.append(list.get(i).getDate().toString());
                fw.append(',');
                int id2 = list.get(i).getId();
                Integer obj2 = id2;
                fw.append(obj2.toString());
                fw.append(',');
                int rid = list.get(i).getRid();
                obj = rid;
                fw.append(obj.toString());
                fw.append(',');
                int sid = list.get(i).getSid();
                obj = sid;
                fw.append(obj.toString());
                fw.append('\n');
            }
            fw.flush();
            fw.close();
            LOGGER.info("CSV File is created successfully.");
            EmailService.sendEmailWithAttachment("","","yashshukla195@gmail.com","",user.getEmail(),"","",filename);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
