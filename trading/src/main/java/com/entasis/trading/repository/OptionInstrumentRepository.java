package com.entasis.trading.repository;

import com.entasis.trading.entity.OptionInstrument;
import com.entasis.trading.entity.OptionSeries;
import com.entasis.trading.entity.enums.OptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface OptionInstrumentRepository extends JpaRepository<OptionInstrument, Long> {
    Optional<OptionInstrument> findBySeriesAndStrikePriceAndOptionType(
        OptionSeries series, 
        BigDecimal strikePrice, 
        OptionType optionType
    );
} 