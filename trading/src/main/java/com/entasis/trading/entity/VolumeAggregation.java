package com.entasis.trading.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.entasis.trading.entity.enums.AggregationPeriod;
import com.entasis.trading.entity.enums.InstrumentType;

@Entity
@Table(name = "volume_aggregations")
@Getter
@Setter
public class VolumeAggregation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "symbol_id", nullable = false)
    private Symbol symbol;
    
    @Column(name = "instrument_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private InstrumentType instrumentType;
    
    @Column(name = "period_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AggregationPeriod periodType;
    
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;
    
    @Column(name = "volume", nullable = false, precision = 30, scale = 8)
    private BigDecimal volume;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
} 