package com.entasis.trading.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class OptionMarketData {
    private String instrumentName;  // BTC-16FEB25-96000-C
    private BigDecimal price;
    private BigDecimal underlyingPrice;
    private BigDecimal impliedVolatility;
    private BigDecimal volume;
    private BigDecimal openInterest;
    private BigDecimal bidPrice;
    private BigDecimal askPrice;
    private Greeks greeks;
    private LocalDateTime timestamp;
} 