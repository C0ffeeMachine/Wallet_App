package com.example.WalletService.Repository;

import com.example.WalletService.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {

    @Query(value = "SELECT t FROM Transaction t WHERE t.sid = ?1 OR t.rid = ?1")
    List<Transaction> findBysidAndrid(int id);
}
