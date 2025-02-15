package com.entasis.trading.repository;

import com.entasis.trading.entity.OptionSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OptionSeriesRepository extends JpaRepository<OptionSeries, Long> {
    Optional<OptionSeries> findByUnderlyingAssetAndExpiryDate(String underlyingAsset, LocalDateTime expiryDate);
} 