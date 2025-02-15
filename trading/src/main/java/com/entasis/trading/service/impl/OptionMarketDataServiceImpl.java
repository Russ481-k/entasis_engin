package com.entasis.trading.service.impl;

import com.entasis.trading.dto.OptionMarketData;
import com.entasis.trading.entity.OptionMarketDataEntity;
import com.entasis.trading.repository.OptionMarketDataRepository;
import com.entasis.trading.service.OptionMarketDataService;
import com.entasis.trading.mapper.OptionMarketDataMapper;
import com.entasis.trading.collector.exchange.DeribitOptionExchange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OptionMarketDataServiceImpl implements OptionMarketDataService {
    
    private final OptionMarketDataRepository optionMarketDataRepository;
    private final DeribitOptionExchange deribitExchange;
    private final OptionMarketDataMapper optionsMarketDataMapper;

    @Override
    @Transactional
    public void syncOptionsInstruments(String currency) {
        log.info("Starting options instrument sync for currency: {}", currency);
        
        List<String> instruments = deribitExchange.getOptionsInstruments(currency);
        log.info("Found {} option instruments for {}", instruments.size(), currency);
        
        for (String instrumentName : instruments) {
            try {
                OptionMarketData marketData = deribitExchange.getOptionsMarketData(instrumentName);
                log.info("Received market data for {}: {}", instrumentName, marketData);
                
                OptionMarketDataEntity entity = optionsMarketDataMapper.toEntity(marketData);
                log.info("Mapped to entity: {}", entity);
                
                optionMarketDataRepository.save(entity);
                log.info("Successfully saved market data for {}", instrumentName);
            } catch (Exception e) {
                log.error("Error collecting options data for {}: {}", instrumentName, e.getMessage());
            }
        }
    }
    
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void collectOptionsMarketData(String instrumentName) {
        log.info("Collecting options market data for instrument: {}", instrumentName);
        try {
            OptionMarketData marketData = deribitExchange.getOptionsMarketData(instrumentName);
            OptionMarketDataEntity entity = optionsMarketDataMapper.toEntity(marketData);
            optionMarketDataRepository.save(entity);
            
            log.info("Collected options market data for {}: price={}, volume={}", 
                instrumentName, marketData.getPrice(), marketData.getVolume());
        } catch (Exception e) {
            log.error("Error collecting options data for {}: {}", instrumentName, e.getMessage());
            throw e;
        }
    }
    
    @Override
    public List<OptionMarketDataEntity> getMarketDataByTimeRange(
        String underlyingAsset, 
        LocalDateTime expiryDate, 
        LocalDateTime startTime, 
        LocalDateTime endTime
    ) {
                
        return optionMarketDataRepository
            .findByInstrument_Series_UnderlyingAssetAndTimestampBetweenOrderByTimestampDesc(
                underlyingAsset, startTime, endTime);
    }
} 