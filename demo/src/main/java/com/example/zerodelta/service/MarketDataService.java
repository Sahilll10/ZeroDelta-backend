package com.example.zerodelta.service;

import com.example.zerodelta.model.Asset;
import com.example.zerodelta.model.AssetClass;
import com.example.zerodelta.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j // Lombok annotation to enable professional console logging
public class MarketDataService {

    private final AssetRepository assetRepository;
    private final RestTemplate restTemplate;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void fetchLivePrices() {
        log.info("Executing background job: Fetching live market data...");

        List<Asset> assets = assetRepository.findAll();

        for (Asset asset : assets) {
            if (asset.getAssetClass() == AssetClass.CRYPTO) {
                updateCryptoPrice(asset);
            }
            else {
                simulateMarketMovement(asset);
            }
        }
        assetRepository.saveAll(assets);
    }

    private void updateCryptoPrice(Asset asset) {
        try {
            String binanceUrl = "https://api.binance.com/api/v3/ticker/price?symbol=" + asset.getTicker() + "USDT";
            Map<String, Object> response = restTemplate.getForObject(binanceUrl, Map.class);

            if (response != null && response.containsKey("price")) {
                BigDecimal newPrice = new BigDecimal(response.get("price").toString());
                asset.setCurrentPrice(newPrice);
                log.info("Updated {} to live price: ${}", asset.getTicker(), newPrice);
            }
        } catch (Exception e) {
            // FAULT TOLERANCE
            log.error("Network failure fetching live price for {}. Using cached price.", asset.getTicker());
        }
    }

    //DEMO PRICWE MOVE
    private void simulateMarketMovement(Asset asset) {
        BigDecimal current = asset.getCurrentPrice();
        double randomDrift = 1.0 + (Math.random() * 0.02 - 0.01);
        asset.setCurrentPrice(current.multiply(BigDecimal.valueOf(randomDrift)));
    }
}