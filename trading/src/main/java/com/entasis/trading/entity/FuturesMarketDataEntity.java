package com.entasis.trading.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "futures_market_data")
@Getter
@Setter
public class FuturesMarketDataEntity extends BaseMarketDataEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_id", nullable = false)
    private ExchangeEntity exchange;
    
    @Column(name = "open_interest")
    private BigDecimal openInterest;
    
    @Column(name = "funding_rate")
    private BigDecimal fundingRate;
    
    @Column(name = "mark_price")
    private BigDecimal markPrice;
    
    @Column(name = "index_price")
    private BigDecimal indexPrice;

    public ExchangeEntity getExchange() {
        return exchange;
    }

    public void setExchange(ExchangeEntity exchange) {
        this.exchange = exchange;
    }
} 