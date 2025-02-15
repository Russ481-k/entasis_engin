package com.entasis.trading.repository;

import com.entasis.trading.entity.OptionMarketDataEntity;
import com.entasis.trading.entity.OptionSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OptionMarketDataRepository extends JpaRepository<OptionMarketDataEntity, Long> {
    List<OptionMarketDataEntity> findByInstrument_Series_UnderlyingAssetAndTimestampBetweenOrderByTimestampDesc(
        String symbol,
        LocalDateTime startTime,
        LocalDateTime endTime
    );
}