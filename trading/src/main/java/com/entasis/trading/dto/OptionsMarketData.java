package com.entasis.trading.dto;

import com.entasis.trading.entity.enums.OptionType;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class OptionsMarketData extends BaseMarketDataDto {
    private BigDecimal underlyingPrice;
    private BigDecimal openInterest;
    private BigDecimal bidPrice;
    private BigDecimal askPrice;
    private BigDecimal impliedVolatility;
    private BigDecimal strikePrice;
    private OptionType optionType;
    private LocalDateTime expiryDate;
    private Greeks greeks;
} 