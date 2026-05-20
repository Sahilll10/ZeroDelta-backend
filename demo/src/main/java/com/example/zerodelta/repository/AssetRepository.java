package com.example.zerodelta.repository;

import com.example.zerodelta.model.Asset;
import com.example.zerodelta.model.AssetClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    Optional<Asset> findByTicker(String ticker);
    List<Asset> findByAssetClass(AssetClass assetClass);
    List<Asset> findByIsTradableTrue();
}
