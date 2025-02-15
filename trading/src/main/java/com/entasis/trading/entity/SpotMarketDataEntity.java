package com.entasis.trading.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "spot_market_data")
@Getter
@Setter
public class SpotMarketDataEntity extends BaseMarketDataEntity {
    @Column(name = "open", precision = 30, scale = 8)
    private BigDecimal open;

    @Column(name = "high", precision = 30, scale = 8)
    private BigDecimal high;

    @Column(name = "low", precision = 30, scale = 8)
    private BigDecimal low;

    @Column(name = "close", precision = 30, scale = 8)
    private BigDecimal close;
} 