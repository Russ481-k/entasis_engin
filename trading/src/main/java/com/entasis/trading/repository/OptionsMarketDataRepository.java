package com.entasis.trading.repository;

import com.entasis.trading.entity.OptionsMarketDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OptionsMarketDataRepository extends JpaRepository<OptionsMarketDataEntity, Long> {
    List<OptionsMarketDataEntity> findBySymbol_ExchangeSymbolOrderByTimestampDesc(String symbol);

    List<OptionsMarketDataEntity> findBySymbol_ExchangeSymbolAndTimestampBetweenOrderByTimestampDesc(
        String symbol, LocalDateTime startTime, LocalDateTime endTime);

    @Query("""
        SELECT COALESCE(SUM(o.volume), 0) 
        FROM OptionsMarketDataEntity o 
        WHERE o.symbol.exchangeSymbol = :symbol 
        AND o.timestamp BETWEEN :startTime AND :endTime
    """)
    BigDecimal sumVolumeByPeriod(String symbol, LocalDateTime startTime, LocalDateTime endTime);
} 