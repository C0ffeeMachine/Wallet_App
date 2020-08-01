package com.example.WalletService.Controller;

import com.example.WalletService.Model.Wallet;
import com.example.WalletService.Repository.TransactionRepository;
import com.example.WalletService.Repository.WalletRepository;
import com.example.WalletService.Util.WalletValidator;
import com.example.WalletService.exception.WalletBadRequest;
import com.example.WalletService.exception.WalletNotFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WalletResource {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    WalletValidator walletValidator = new WalletValidator();
    private static final Logger logger = LoggerFactory.getLogger(WalletResource.class);

    @GetMapping(value = "/wallet")
    @ApiOperation(value = "Find all the wallet")
    List<Wallet> findAllWallet(){
        return walletRepository.findAll();
    }

    @GetMapping("/wallet/{id}")
    @ApiOperation(value = "Find a wallet by id")
    Wallet findOneWallet(@ApiParam(value = "Store id of the point of service to deliver to/collect from",required = true)@PathVariable int id){
        logger.info("/wallet/{id} called with id: "+id);
        return walletRepository.findById(id).orElseThrow(()->new WalletNotFoundException(id));
    }

    @PostMapping(value = "/wallet")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Adds a new wallet")
    Wallet createNewWallet(@RequestBody Wallet wallet){
        if(!walletValidator.validateWalletRequest(wallet)){
            logger.info("New wallet is not valid");
            throw new WalletBadRequest();
        }else {
            return walletRepository.save(wallet);
        }
    }

    @PutMapping(value = "/wallet")
    @ApiOperation(value = "Update the wallet")
    Wallet updateWallet(@RequestBody Wallet wallet){
        return walletRepository.save(wallet);
    }
}
