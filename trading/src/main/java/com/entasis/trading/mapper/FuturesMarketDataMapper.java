package com.entasis.trading.mapper;

import com.entasis.trading.dto.FuturesMarketData;
import com.entasis.trading.entity.ExchangeEntity;
import com.entasis.trading.entity.FuturesMarketDataEntity;
import com.entasis.trading.entity.SymbolEntity;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class FuturesMarketDataMapper {
    
    public FuturesMarketDataEntity toEntity(FuturesMarketData dto, SymbolEntity symbol, ExchangeEntity exchange) {
        FuturesMarketDataEntity entity = new FuturesMarketDataEntity();
        entity.setSymbol(symbol);
        entity.setExchange(exchange);
        entity.setTimestamp(dto.getTimestamp());
        entity.setPrice(dto.getPrice());
        entity.setVolume(dto.getVolume());
        entity.setOpen(dto.getOpen());
        entity.setHigh(dto.getHigh());
        entity.setLow(dto.getLow());
        entity.setClose(dto.getClose());
        entity.setOpenInterest(dto.getOpenInterest());
        entity.setFundingRate(dto.getFundingRate());
        entity.setMarkPrice(dto.getMarkPrice());
        entity.setIndexPrice(dto.getIndexPrice());
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }

    public FuturesMarketData toDto(FuturesMarketDataEntity entity) {
        FuturesMarketData dto = new FuturesMarketData();
        dto.setSymbol(entity.getSymbol().getSymbol());
        dto.setExchange(entity.getExchange().getName());
        dto.setTimestamp(entity.getTimestamp());
        dto.setPrice(entity.getPrice());
        dto.setVolume(entity.getVolume());
        dto.setOpen(entity.getOpen());
        dto.setHigh(entity.getHigh());
        dto.setLow(entity.getLow());
        dto.setClose(entity.getClose());
        dto.setOpenInterest(entity.getOpenInterest());
        dto.setFundingRate(entity.getFundingRate());
        dto.setMarkPrice(entity.getMarkPrice());
        dto.setIndexPrice(entity.getIndexPrice());
        return dto;
    }
} 