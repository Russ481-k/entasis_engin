package com.entasis.trading.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseMarketDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "symbol_id", nullable = false)
    private Symbol symbol;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    
    @Column(name = "volume", precision = 30, scale = 8)
    private BigDecimal volume;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
} 