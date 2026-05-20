package com.example.zerodelta.controller;

import com.example.zerodelta.dto.PortfolioSummaryResponse;
import com.example.zerodelta.model.Asset;
import com.example.zerodelta.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final AssetRepository assetRepository;

    // FRONTEND HIT
    @GetMapping("/markets")
    public ResponseEntity<List<Asset>> getLiveMarkets() {
        return ResponseEntity.ok(assetRepository.findByIsTradableTrue());
    }

    //SERVER TEST
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("ZeroDelta Backend is fully operational");
    }
}