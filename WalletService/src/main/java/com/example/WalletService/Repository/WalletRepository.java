package com.example.WalletService.Repository;

import com.example.WalletService.Model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WalletRepository extends JpaRepository<Wallet,Integer> {

    @Query(value = "SELECT w FROM Wallet w WHERE w.user_id = :userId")
    Wallet findWalletByUserId(int userId);
}
