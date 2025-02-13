package com.entasis.trading.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "symbols")
@Getter @Setter
public class SymbolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String symbol;
    
    @Column(nullable = false)
    private String baseAsset;
    
    @Column(nullable = false)
    private String quoteAsset;
    
    private LocalDateTime createdAt;
} 