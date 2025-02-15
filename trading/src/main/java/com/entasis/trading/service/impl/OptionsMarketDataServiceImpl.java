package com.entasis.trading.service.impl;

import com.entasis.trading.dto.OptionsMarketData;
import com.entasis.trading.entity.OptionsMarketDataEntity;
import com.entasis.trading.entity.Symbol;
import com.entasis.trading.entity.enums.InstrumentType;
import com.entasis.trading.entity.enums.OptionType;
import com.entasis.trading.repository.OptionsMarketDataRepository;
import com.entasis.trading.repository.SymbolRepository;
import com.entasis.trading.repository.ExchangeRepository;
import com.entasis.trading.service.OptionsMarketDataService;
import com.entasis.trading.mapper.OptionsMarketDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entasis.trading.service.BaseMarketDataService;
import com.entasis.trading.entity.ExchangeEntity;
import com.entasis.trading.entity.enums.ExchangeType;
import com.entasis.trading.exception.DeribitApiException;
import com.entasis.trading.collector.exchange.DeribitOptionsExchange;
import com.entasis.trading.dto.OptionsInstrument;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Slf4j
@Service
@Transactional(readOnly = true)
public class OptionsMarketDataServiceImpl extends BaseMarketDataService<OptionsMarketData> 
    implements OptionsMarketDataService {
    
    private final OptionsMarketDataRepository optionsMarketDataRepository;
    private final DeribitOptionsExchange deribitExchange;
    private final OptionsMarketDataMapper optionsMarketDataMapper;
    private final SymbolRepository symbolRepository;
    private final ExchangeRepository exchangeRepository;

    public OptionsMarketDataServiceImpl(
        SymbolRepository symbolRepository,
        ExchangeRepository exchangeRepository,
        OptionsMarketDataRepository optionsMarketDataRepository,
        DeribitOptionsExchange deribitExchange,
        OptionsMarketDataMapper optionsMarketDataMapper
    ) {
        this.optionsMarketDataRepository = optionsMarketDataRepository;
        this.deribitExchange = deribitExchange;
        this.optionsMarketDataMapper = optionsMarketDataMapper;
        this.symbolRepository = symbolRepository;
        this.exchangeRepository = exchangeRepository;
    }

    @Override
    @Transactional
    public void syncOptionsInstruments(String currency) {
        log.info("Starting options instrument sync for currency: {}", currency);
        try {
            List<OptionsInstrument> instruments = deribitExchange.getInstruments(currency);
            if (instruments.isEmpty()) {
                log.warn("No instruments found for currency: {}", currency);
                return;
            }

            instruments.forEach(instrument -> {
                try {
                    OptionsMarketData marketData = deribitExchange.getOptionsMarketData(instrument.getInstrumentName());
                    save(marketData);
                } catch (Exception e) {
                    log.error("Error collecting options data for {}: {}", 
                        instrument.getInstrumentName(), e.getMessage());
                }
            });
        } catch (Exception e) {
            log.error("Deribit API error during sync: {}", e.getMessage());
            throw new RuntimeException("Failed to sync options instruments", e);
        }
    }
    
    @Override
    @Transactional
    public void collectOptionsMarketData(String instrumentName) {
        log.info("Collecting options market data for instrument: {}", instrumentName);
        try {
            OptionsMarketData optionsData = deribitExchange.getOptionsMarketData(instrumentName);
            save(optionsData);
            log.info("Collected options market data for {}: price={}, volume={}", 
                instrumentName, optionsData.getPrice(), optionsData.getVolume());
        } catch (Exception e) {
            log.error("Error processing options data for {}: {}", instrumentName, e.getMessage());
            throw e;
        }
    }
    
    @Override
    public List<OptionsMarketData> getMarketDataBySymbol(String symbol, LocalDateTime startTime, LocalDateTime endTime) {
        return optionsMarketDataRepository.findBySymbol_ExchangeSymbolAndTimestampBetweenOrderByTimestampDesc(
            symbol, startTime, endTime)
            .stream()
            .map(optionsMarketDataMapper::toDto)
            .collect(Collectors.toList());
    }
    
    private void saveOptionsInfo(String currency, String expiration, String strike, String type) {
        String instrumentName = String.format("%s-%s-%s-%s", currency, expiration, strike, type);
        Symbol symbol = getOrCreateSymbol(instrumentName, InstrumentType.OPTION);
        
        // 기본 옵션 정보 저장
        OptionsMarketData optionsData = new OptionsMarketData();
        optionsData.setSymbol(instrumentName);
        optionsData.setTimestamp(LocalDateTime.now());
        optionsData.setStrikePrice(new BigDecimal(strike));
        optionsData.setOptionType("CALL".equals(type) ? OptionType.CALL : OptionType.PUT);
        optionsData.setExpiryDate(LocalDateTime.parse(expiration));
        
        OptionsMarketDataEntity entity = optionsMarketDataMapper.toEntity(optionsData, symbol);
        optionsMarketDataRepository.save(entity);
    }

    @Override
    @Transactional
    public OptionsMarketData save(OptionsMarketData marketData) {
        return super.save(marketData);
    }

    @Override
    public List<OptionsMarketData> findBySymbol(String symbol) {
        return optionsMarketDataRepository.findBySymbol_ExchangeSymbolOrderByTimestampDesc(symbol)
            .stream()
            .map(optionsMarketDataMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<OptionsMarketData> findBySymbolAndTimeRange(String symbol, LocalDateTime startTime, LocalDateTime endTime) {
        return optionsMarketDataRepository.findBySymbol_ExchangeSymbolAndTimestampBetweenOrderByTimestampDesc(
            symbol, startTime, endTime)
            .stream()
            .map(optionsMarketDataMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public Symbol getOrCreateSymbol(String symbolName, InstrumentType instrumentType) {
        return symbolRepository.findByExchangeSymbolAndInstrumentTypeAndExchange_Type(
            symbolName, 
            instrumentType, 
            ExchangeType.OPTION)
            .orElseGet(() -> {
                ExchangeEntity exchange = exchangeRepository.findByNameAndType(
                    "deribit", 
                    ExchangeType.valueOf(instrumentType.name()))
                    .orElseThrow(() -> new RuntimeException(
                        String.format("Exchange not found: deribit/%s", instrumentType)));
                
                Symbol newSymbol = new Symbol();
                newSymbol.setExchange(exchange);
                newSymbol.setExchangeSymbol(symbolName);
                String[] parts = symbolName.split("-");
                newSymbol.setBaseAsset(parts[0]);
                newSymbol.setQuoteAsset("USD");
                newSymbol.setInstrumentType(instrumentType);
                
                return symbolRepository.save(newSymbol);
            });
    }

    @Override
    protected InstrumentType getInstrumentType() {
        return InstrumentType.OPTION;
    }

    @Override
    protected void saveInternal(OptionsMarketData marketData, Symbol symbol) {
        OptionsMarketDataEntity entity = optionsMarketDataMapper.toEntity(marketData, symbol);
        optionsMarketDataRepository.save(entity);
    }
} 