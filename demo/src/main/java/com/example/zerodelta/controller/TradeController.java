package com.example.zerodelta.controller;

import com.example.zerodelta.dto.OrderRequest;
import com.example.zerodelta.model.TransactionOrder;
import com.example.zerodelta.service.TradingService;
import com.example.zerodelta.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor

//BACKEND FRONTEND CONNECT
@CrossOrigin(origins = "*")

public class TradeController {

    private final TradingService tradingService;
    private final WalletService walletService;

    @PostMapping("/trade")
    public ResponseEntity<TransactionOrder> executeTrade(@Valid @RequestBody OrderRequest request) {
        TransactionOrder executedOrder = tradingService.executeTrade(request);
        return ResponseEntity.ok(executedOrder);
    }


    @PostMapping("/fiat")
    public ResponseEntity<TransactionOrder> executeFiatTransfer(@Valid @RequestBody OrderRequest request) {
        TransactionOrder executedOrder = walletService.processFiatTransaction(request);
        return ResponseEntity.ok(executedOrder);
    }
}