package com.entasis.trading.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "option_market_data")
@Getter @Setter
@NoArgsConstructor
public class OptionMarketDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "instrument_id")
    private OptionInstrument instrument;
    
    @ManyToOne
    @JoinColumn(name = "symbol_id")
    private Symbol symbol;
    
    @Column(name = "price", precision = 30, scale = 8)
    private BigDecimal price;
    
    @Column(name = "underlying_price", precision = 30, scale = 8)
    private BigDecimal underlyingPrice;
    
    @Column(name = "implied_volatility", precision = 30, scale = 8)
    private BigDecimal impliedVolatility;
    
    @Column(name = "delta", precision = 30, scale = 8)
    private BigDecimal delta;
    
    @Column(name = "gamma", precision = 30, scale = 8)
    private BigDecimal gamma;
    
    @Column(name = "theta", precision = 30, scale = 8)
    private BigDecimal theta;
    
    @Column(name = "vega", precision = 30, scale = 8)
    private BigDecimal vega;
    
    @Column(name = "rho", precision = 30, scale = 8)
    private BigDecimal rho;
    
    @Column(name = "volume", precision = 30, scale = 8)
    private BigDecimal volume;
    
    @Column(name = "open_interest", precision = 30, scale = 8)
    private BigDecimal openInterest;
    
    @Column(name = "bid_price", precision = 30, scale = 8)
    private BigDecimal bidPrice;
    
    @Column(name = "ask_price", precision = 30, scale = 8)
    private BigDecimal askPrice;
    
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 