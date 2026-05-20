package com.example.zerodelta.repository;

import com.example.zerodelta.model.PortfolioHolding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioHoldingRepository extends JpaRepository<PortfolioHolding, Long> {
    List<PortfolioHolding> findByUserId(Long userId);
    Optional<PortfolioHolding> findByUserIdAndAssetId(Long userId, Long AssetId);
}
