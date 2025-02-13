package com.entasis.trading.repository;

import com.entasis.trading.entity.SpotMarketDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SpotMarketDataRepository extends JpaRepository<SpotMarketDataEntity, Long> {
    List<SpotMarketDataEntity> findBySymbol_SymbolAndExchange_NameOrderByTimestampDesc(String symbol, String exchange);
    List<SpotMarketDataEntity> findBySymbol_SymbolOrderByTimestampDesc(String symbol);
    List<SpotMarketDataEntity> findBySymbol_SymbolAndTimestampBetweenOrderByTimestampDesc(
        String symbol, LocalDateTime startTime, LocalDateTime endTime);
    List<SpotMarketDataEntity> findByExchange_NameOrderByTimestampDesc(String exchange);
} 