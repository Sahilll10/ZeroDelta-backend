package com.example.zerodelta.service;

import com.example.zerodelta.dto.OrderRequest;
import com.example.zerodelta.exception.InsufficientFundException;
import com.example.zerodelta.exception.ResourceNotFoundException;
import com.example.zerodelta.model.*;
import com.example.zerodelta.repository.TransactionOrderRepository;
import com.example.zerodelta.repository.UserRepository;
import com.example.zerodelta.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionOrderRepository orderRepository;
    private final UserRepository userRepository;


    @Transactional
    public TransactionOrder processFiatTransaction(OrderRequest request) {

       //IDEMPOTENCY CHECK
        Optional<TransactionOrder> existingOrder = orderRepository.findByIdempotencyKey(request.getIdempotencyKey());
        if (existingOrder.isPresent()) {
            return existingOrder.get();
        }


        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User ID " + request.getUserId() + " not found."));
        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet for User " + user.getId() + " not found."));



        if (request.getOrderType() == OrderType.WITHDRAW) {
            //CHECK
            if (wallet.getFiatBalance().compareTo(request.getAmount()) < 0) {
                throw new InsufficientFundException("Withdrawal failed: Insufficient fiat balance.");
            }
            wallet.setFiatBalance(wallet.getFiatBalance().subtract(request.getAmount()));

        } else if (request.getOrderType() == OrderType.DEPOSIT) {
            wallet.setFiatBalance(wallet.getFiatBalance().add(request.getAmount()));
        } else {
            throw new IllegalArgumentException("WalletService only handles DEPOSIT or WITHDRAW order types.");
        }

        walletRepository.save(wallet);

        TransactionOrder newOrder = TransactionOrder.builder()
                .idempotencyKey(request.getIdempotencyKey())
                .user(user)
                .orderType(request.getOrderType())
                .status(OrderStatus.EXECUTED)
                .fiatAmount(request.getAmount())
                .build();

        return orderRepository.save(newOrder);
    }
}