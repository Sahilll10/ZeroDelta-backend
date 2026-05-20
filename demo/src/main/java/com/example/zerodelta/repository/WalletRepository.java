package com.example.zerodelta.repository;

import com.example.zerodelta.model.Wallet;
import jakarta.persistence.PreUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUserId(Long userId);
}
