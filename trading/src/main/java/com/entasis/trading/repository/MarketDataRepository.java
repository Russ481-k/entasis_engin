package com.entasis.trading.repository;

import com.entasis.trading.entity.MarketDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface MarketDataRepository extends JpaRepository<MarketDataEntity, Long> {
    
    List<MarketDataEntity> findBySymbolOrderByTimestampDesc(String symbol);
    
    @Query("SELECT m FROM MarketDataEntity m WHERE m.symbol = :symbol AND m.timestamp BETWEEN :startTime AND :endTime ORDER BY m.timestamp DESC")
    List<MarketDataEntity> findBySymbolAndTimeRange(
        @Param("symbol") String symbol,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    List<MarketDataEntity> findByExchangeOrderByTimestampDesc(String exchange);
} 