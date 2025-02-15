package com.entasis.trading.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.entasis.trading.entity.enums.OptionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "option_instruments")
@Getter @Setter
@NoArgsConstructor
public class OptionInstrument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "series_id")
    private OptionSeries series;
    
    private BigDecimal strikePrice;
    
    @Enumerated(EnumType.STRING)
    private OptionType optionType;
    
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "instrument")
    private List<OptionMarketDataEntity> marketData;
} 