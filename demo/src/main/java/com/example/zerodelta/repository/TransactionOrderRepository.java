package com.example.zerodelta.repository;

import com.example.zerodelta.model.TransactionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionOrderRepository extends JpaRepository<TransactionOrder,Long> {

    List<TransactionOrder> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<TransactionOrder>findByIdempotencyKey(String idempotencyKey);
}
