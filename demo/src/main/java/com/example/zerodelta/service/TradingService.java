package com.example.zerodelta.service;

import com.example.zerodelta.dto.OrderRequest;
import com.example.zerodelta.exception.InsufficientFundException;
import com.example.zerodelta.exception.ResourceNotFoundException;
import com.example.zerodelta.model.*;
import com.example.zerodelta.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TradingService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final AssetRepository assetRepository;
    private final PortfolioHoldingRepository holdingRepository;
    private final TransactionOrderRepository orderRepository;


    @Transactional
    public TransactionOrder executeTrade(OrderRequest request) {

        //Idempotency Check
        Optional<TransactionOrder> existingOrder = orderRepository.findByIdempotencyKey(request.getIdempotencyKey());
        if (existingOrder.isPresent()) {
            return existingOrder.get();
        }

        //Details Fetch
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
        Asset asset = assetRepository.findByTicker(request.getTicker())
                .orElseThrow(() -> new ResourceNotFoundException("Asset " + request.getTicker() + " not found"));

        if (!asset.isTradable()) {
            throw new IllegalStateException("Asset " + asset.getTicker() + " is currently halted for trading.");
        }

        BigDecimal tradeFiatAmount = request.getAmount();
        BigDecimal currentPrice = asset.getCurrentPrice();
        //Rounding for Division
        BigDecimal unitsInvolved = tradeFiatAmount.divide(currentPrice, 8, RoundingMode.HALF_UP);

        //Check Pre exists
        Optional<PortfolioHolding> existingHoldingOpt = holdingRepository.findByUserIdAndAssetId(user.getId(), asset.getId());
        PortfolioHolding holding;

        //BUY
        if (request.getOrderType() == OrderType.BUY) {
            if (wallet.getFiatBalance().compareTo(tradeFiatAmount) < 0) {
                throw new InsufficientFundException("Insufficient fiat balance to execute buy order.");
            }

            wallet.setFiatBalance(wallet.getFiatBalance().subtract(tradeFiatAmount));

            // UPSERT
            if (existingHoldingOpt.isPresent()) {
                holding = existingHoldingOpt.get();

                // AVG BUY PRICE
                BigDecimal totalCostBefore = holding.getUnits().multiply(holding.getAverageBuyPrice());
                BigDecimal newTotalCost = totalCostBefore.add(tradeFiatAmount);
                BigDecimal newTotalUnits = holding.getUnits().add(unitsInvolved);
                holding.setAverageBuyPrice(newTotalCost.divide(newTotalUnits, 4, RoundingMode.HALF_UP));
                holding.setUnits(newTotalUnits);
            } else {
                holding = PortfolioHolding.builder()
                        .user(user)
                        .asset(asset)
                        .units(unitsInvolved)
                        .averageBuyPrice(currentPrice)
                        .build();
            }
        //SELL
        } else if (request.getOrderType() == OrderType.SELL) {
            holding = existingHoldingOpt.orElseThrow(() ->
                    new IllegalStateException("Cannot sell an asset you do not own."));
            if (holding.getUnits().compareTo(unitsInvolved) < 0) {
                throw new InsufficientFundException("Insufficient asset units to execute sell order.");
            }

            wallet.setFiatBalance(wallet.getFiatBalance().add(tradeFiatAmount));
            holding.setUnits(holding.getUnits().subtract(unitsInvolved));

        } else {
            throw new IllegalArgumentException("TradingService only handles BUY or SELL order types.");
        }

        //SAVEE
        walletRepository.save(wallet);
        holdingRepository.save(holding);

        //LEDGER ENTRY
        TransactionOrder newOrder = TransactionOrder.builder()
                .idempotencyKey(request.getIdempotencyKey())
                .user(user)
                .asset(asset)
                .orderType(request.getOrderType())
                .status(OrderStatus.EXECUTED)
                .fiatAmount(tradeFiatAmount)
                .assetUnits(unitsInvolved)
                .executionPrice(currentPrice)
                .build();

        return orderRepository.save(newOrder);
    }
}