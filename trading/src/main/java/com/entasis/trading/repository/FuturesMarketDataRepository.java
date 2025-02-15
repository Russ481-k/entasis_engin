package com.entasis.trading.repository;

import com.entasis.trading.entity.FuturesMarketDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FuturesMarketDataRepository extends JpaRepository<FuturesMarketDataEntity, Long> {
    List<FuturesMarketDataEntity> findBySymbol_ExchangeSymbolAndExchange_NameOrderByTimestampDesc(
        String symbol, String exchange);
    
    List<FuturesMarketDataEntity> findBySymbol_ExchangeSymbolOrderByTimestampDesc(String symbol);
    
    List<FuturesMarketDataEntity> findBySymbol_ExchangeSymbolAndTimestampBetweenOrderByTimestampDesc(
        String symbol, LocalDateTime startTime, LocalDateTime endTime);
    
    List<FuturesMarketDataEntity> findByExchange_NameOrderByTimestampDesc(String exchange);

    @Query("""
        SELECT COALESCE(SUM(f.volume), 0) 
        FROM FuturesMarketDataEntity f 
        WHERE f.symbol.exchangeSymbol = :symbol 
        AND f.timestamp BETWEEN :startTime AND :endTime
    """)
    BigDecimal sumVolumeByPeriod(String symbol, LocalDateTime startTime, LocalDateTime endTime);
    
    @Query("""
        SELECT f FROM FuturesMarketDataEntity f 
        WHERE f.symbol.exchangeSymbol = :symbol 
        AND f.timestamp BETWEEN :startTime AND :endTime 
        ORDER BY f.timestamp DESC
    """)
    List<FuturesMarketDataEntity> findMarketDataByPeriod(String symbol, LocalDateTime startTime, LocalDateTime endTime);
} 