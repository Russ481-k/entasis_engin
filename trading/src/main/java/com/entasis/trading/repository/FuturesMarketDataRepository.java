package com.entasis.trading.repository;

import com.entasis.trading.entity.FuturesMarketDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FuturesMarketDataRepository extends JpaRepository<FuturesMarketDataEntity, Long> {
    List<FuturesMarketDataEntity> findBySymbol_SymbolAndExchange_NameOrderByTimestampDesc(String symbol, String exchange);
    List<FuturesMarketDataEntity> findBySymbol_SymbolOrderByTimestampDesc(String symbol);
    List<FuturesMarketDataEntity> findBySymbol_SymbolAndTimestampBetweenOrderByTimestampDesc(
        String symbol, LocalDateTime startTime, LocalDateTime endTime);
    List<FuturesMarketDataEntity> findByExchange_NameOrderByTimestampDesc(String exchange);
} 