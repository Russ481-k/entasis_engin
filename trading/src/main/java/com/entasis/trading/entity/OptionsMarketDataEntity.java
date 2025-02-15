package com.entasis.trading.entity;

import com.entasis.trading.entity.enums.OptionType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "options_market_data")
public class OptionsMarketDataEntity extends BaseMarketDataEntity {
    private BigDecimal underlyingPrice;
    private BigDecimal openInterest;
    private BigDecimal bidPrice;
    private BigDecimal askPrice;
    private BigDecimal impliedVolatility;
    private BigDecimal strikePrice;
    
    @Enumerated(EnumType.STRING)
    private OptionType optionType;
    
    private LocalDateTime expiryDate;
    
    // Greeks
    private BigDecimal delta;
    private BigDecimal gamma;
    private BigDecimal vega;
    private BigDecimal theta;
    private BigDecimal rho;
} 