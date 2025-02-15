package com.entasis.trading.repository;

import com.entasis.trading.entity.VolumeAggregation;
import com.entasis.trading.entity.enums.AggregationPeriod;
import com.entasis.trading.entity.enums.InstrumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VolumeAggregationRepository extends JpaRepository<VolumeAggregation, Long> {
    
    @Query("""
        SELECT v FROM VolumeAggregation v 
        WHERE v.symbol.exchangeSymbol = :symbol 
        AND v.instrumentType = :instrumentType 
        AND v.periodType = :periodType 
        AND v.startTime >= :startTime 
        AND v.endTime <= :endTime
        ORDER BY v.startTime DESC
    """)
    List<VolumeAggregation> findVolumeAggregations(
        String symbol,
        InstrumentType instrumentType,
        AggregationPeriod periodType,
        LocalDateTime startTime,
        LocalDateTime endTime
    );
    
    @Query("""
        SELECT SUM(v.volume) FROM VolumeAggregation v 
        WHERE v.symbol.exchangeSymbol = :symbol 
        AND v.instrumentType = :instrumentType 
        AND v.startTime >= :startTime 
        AND v.endTime <= :endTime
    """)
    BigDecimal sumVolume(
        String symbol,
        InstrumentType instrumentType,
        LocalDateTime startTime,
        LocalDateTime endTime
    );
} 