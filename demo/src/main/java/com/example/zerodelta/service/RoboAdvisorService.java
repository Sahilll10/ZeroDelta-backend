package com.example.zerodelta.service;

import com.example.zerodelta.model.*;
import com.example.zerodelta.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoboAdvisorService {

    private final UserRepository userRepository;
    private final PortfolioHoldingRepository holdingRepository;

  //CRON EXPRESSION
    @Scheduled(fixedRate = 12000)
    public void scanAndRebalancePortfolios() {
        log.info("Initiating Robo-Advisor Portfolio Scan...");

        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            analyzePortfolio(user);
        }
    }

    private void analyzePortfolio(User user) {
        List<PortfolioHolding> holdings = holdingRepository.findByUserId(user.getId());

        BigDecimal totalPortfolioValue = BigDecimal.ZERO;

        // 1 Calc total wealth
        for (PortfolioHolding holding : holdings) {
            BigDecimal holdingValue = holding.getUnits().multiply(holding.getAsset().getCurrentPrice());
            totalPortfolioValue = totalPortfolioValue.add(holdingValue);
        }

        if (totalPortfolioValue.compareTo(BigDecimal.ZERO) == 0) return;

        // 2 Calc Delta
        for (PortfolioHolding holding : holdings) {
            BigDecimal holdingValue = holding.getUnits().multiply(holding.getAsset().getCurrentPrice());


            //PERCENTAGE ALLOCATION
            BigDecimal actualPercentage = holdingValue.divide(totalPortfolioValue, 4, RoundingMode.HALF_UP)
                                                      .multiply(new BigDecimal("100"));


            log.info("User {} owns {}% in {}.", user.getId(),actualPercentage,
                                                holding.getAsset().getTicker());
        }
    }
}