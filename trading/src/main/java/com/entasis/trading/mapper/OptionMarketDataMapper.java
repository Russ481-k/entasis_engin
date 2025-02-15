package com.entasis.trading.mapper;

import com.entasis.trading.dto.OptionMarketData;
import com.entasis.trading.entity.OptionSeries;
import com.entasis.trading.entity.OptionInstrument;
import com.entasis.trading.entity.OptionMarketDataEntity;
import com.entasis.trading.entity.enums.OptionType;
import com.entasis.trading.repository.OptionSeriesRepository;
import com.entasis.trading.repository.OptionInstrumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OptionMarketDataMapper {
    
    private final OptionSeriesRepository optionSeriesRepository;
    private final OptionInstrumentRepository optionInstrumentRepository;

    public OptionMarketDataEntity toEntity(OptionMarketData dto) {
        // 1. Parse instrument name (e.g., "BTC-16FEB25-96000-C")
        String[] parts = dto.getInstrumentName().split("-");
        String underlying = parts[0];
        String expiryStr = parts[1];
        BigDecimal strikePrice = new BigDecimal(parts[2]);
        OptionType optionType = parts[3].equals("C") ? OptionType.CALL : OptionType.PUT;

        // 2. Get or create OptionSeries
        LocalDateTime expiryDate = parseExpiryDate(expiryStr);
        OptionSeries series = optionSeriesRepository
            .findByUnderlyingAssetAndExpiryDate(underlying, expiryDate)
            .orElseGet(() -> {
                OptionSeries newSeries = new OptionSeries();
                newSeries.setUnderlyingAsset(underlying);
                newSeries.setExpiryDate(expiryDate);
                newSeries.setCreatedAt(LocalDateTime.now());
                newSeries.setUpdatedAt(LocalDateTime.now());
                return optionSeriesRepository.save(newSeries);
            });

        // 3. Get or create OptionInstrument
        OptionInstrument instrument = optionInstrumentRepository
            .findBySeriesAndStrikePriceAndOptionType(series, strikePrice, optionType)
            .orElseGet(() -> {
                OptionInstrument newInstrument = new OptionInstrument();
                newInstrument.setSeries(series);
                newInstrument.setStrikePrice(strikePrice);
                newInstrument.setOptionType(optionType);
                newInstrument.setCreatedAt(LocalDateTime.now());
                return optionInstrumentRepository.save(newInstrument);
            });

        // 4. Create OptionMarketData
        OptionMarketDataEntity marketData = new OptionMarketDataEntity();
        marketData.setInstrument(instrument);
        marketData.setPrice(dto.getPrice());
        marketData.setUnderlyingPrice(dto.getUnderlyingPrice());
        marketData.setImpliedVolatility(dto.getImpliedVolatility());
        marketData.setVolume(dto.getVolume());
        marketData.setOpenInterest(dto.getOpenInterest());
        marketData.setBidPrice(dto.getBidPrice());
        marketData.setAskPrice(dto.getAskPrice());
        
        if (dto.getGreeks() != null) {
            marketData.setDelta(dto.getGreeks().getDelta());
            marketData.setGamma(dto.getGreeks().getGamma());
            marketData.setTheta(dto.getGreeks().getTheta());
            marketData.setVega(dto.getGreeks().getVega());
            marketData.setRho(dto.getGreeks().getRho());
        }
        
        marketData.setTimestamp(LocalDateTime.now());
        marketData.setCreatedAt(LocalDateTime.now());
        
        return marketData;
    }

    private LocalDateTime parseExpiryDate(String expiryStr) {
        // Parse date like "16FEB25"
        int day = Integer.parseInt(expiryStr.substring(0, 2));
        String monthStr = expiryStr.substring(2, 5);
        int year = 2000 + Integer.parseInt(expiryStr.substring(5));  // 25 -> 2025
        
        // 월 이름 매핑
        Map<String, Integer> monthMap = new HashMap<>();
        monthMap.put("JAN", 1);
        monthMap.put("FEB", 2);
        monthMap.put("MAR", 3);
        monthMap.put("APR", 4);
        monthMap.put("MAY", 5);
        monthMap.put("JUN", 6);
        monthMap.put("JUL", 7);
        monthMap.put("AUG", 8);
        monthMap.put("SEP", 9);
        monthMap.put("OCT", 10);
        monthMap.put("NOV", 11);
        monthMap.put("DEC", 12);
        
        Integer month = monthMap.get(monthStr);
        if (month == null) {
            throw new IllegalArgumentException("Invalid month: " + monthStr);
        }
        
        return LocalDateTime.of(year, month, day, 8, 0); // UTC+8 기준 만기
    }
} 