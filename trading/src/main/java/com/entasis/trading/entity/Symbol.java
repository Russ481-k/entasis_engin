package com.entasis.trading.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import com.entasis.trading.entity.enums.InstrumentType;
import com.entasis.trading.entity.enums.SymbolStatus;

@Entity
@Table(name = "symbols")
@Getter
@Setter
public class Symbol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_id", nullable = false)
    private ExchangeEntity exchange;
    
    @Column(name = "exchange_symbol", nullable = false)
    private String exchangeSymbol;
    
    @Column(name = "base_asset", nullable = false)
    private String baseAsset;
    
    @Column(name = "quote_asset", nullable = false)
    private String quoteAsset;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "instrument_type", nullable = false)
    private InstrumentType instrumentType;
    
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private SymbolStatus status = SymbolStatus.ACTIVE;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
} 