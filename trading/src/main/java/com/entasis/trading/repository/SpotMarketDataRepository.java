package com.entasis.trading.repository;

import com.entasis.trading.entity.SpotMarketDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SpotMarketDataRepository extends JpaRepository<SpotMarketDataEntity, Long> {
    List<SpotMarketDataEntity> findBySymbol_ExchangeSymbolAndSymbol_Exchange_NameOrderByTimestampDesc(
        String symbol, String exchange);
    
    List<SpotMarketDataEntity> findBySymbol_ExchangeSymbolOrderByTimestampDesc(String symbol);
    
    List<SpotMarketDataEntity> findBySymbol_ExchangeSymbolAndTimestampBetweenOrderByTimestampDesc(
        String symbol, LocalDateTime startTime, LocalDateTime endTime);
    
    List<SpotMarketDataEntity> findBySymbol_Exchange_NameOrderByTimestampDesc(String exchange);

    @Query("""
        SELECT COALESCE(SUM(s.volume), 0) 
        FROM SpotMarketDataEntity s 
        WHERE s.symbol.exchangeSymbol = :symbol 
        AND s.timestamp BETWEEN :startTime AND :endTime
    """)
    BigDecimal sumVolumeByPeriod(String symbol, LocalDateTime startTime, LocalDateTime endTime);
} 