package com.entasis.trading.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "futures_market_data")
@Getter @Setter
public class FuturesMarketDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "symbol_id", nullable = false)
    private SymbolEntity symbol;
    
    @ManyToOne
    @JoinColumn(name = "exchange_id", nullable = false)
    private ExchangeEntity exchange;
    
    private LocalDateTime timestamp;
    private BigDecimal price;
    private BigDecimal volume;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private BigDecimal openInterest;
    private BigDecimal fundingRate;
    private BigDecimal markPrice;
    private BigDecimal indexPrice;
    private LocalDateTime createdAt;
} 