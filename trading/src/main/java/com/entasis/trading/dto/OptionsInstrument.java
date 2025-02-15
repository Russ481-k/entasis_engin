package com.entasis.trading.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class OptionsInstrument {
    private String instrumentName;
    private String settlementPeriod;
    private Long expirationTimestamp;
    private BigDecimal strike;
    private String optionType;  // "call" or "put"
} 