package com.entasis.trading.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "market_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 20)
    private String symbol;
    
    @Column(nullable = false, length = 20)
    private String exchange;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal price;
    
    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal volume;
    
    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal open;
    
    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal high;
    
    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal low;
    
    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal close;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
} 