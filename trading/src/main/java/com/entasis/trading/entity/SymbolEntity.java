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
public class SymbolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "base_asset", nullable = false)
    private String baseAsset;
    
    @Column(name = "quote_asset", nullable = false)
    private String quoteAsset;
    
    @Column(name = "exchange_symbol", nullable = false)
    private String exchangeSymbol;
    
    @ManyToOne
    @JoinColumn(name = "exchange_id", nullable = false)
    private ExchangeEntity exchange;
    
    @Column(name = "instrument_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private InstrumentType instrumentType;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SymbolStatus status = SymbolStatus.ACTIVE;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
} 