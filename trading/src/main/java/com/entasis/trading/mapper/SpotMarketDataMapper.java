package com.entasis.trading.mapper;

import com.entasis.trading.dto.SpotMarketData;
import com.entasis.trading.entity.ExchangeEntity;
import com.entasis.trading.entity.SpotMarketDataEntity;
import com.entasis.trading.entity.SymbolEntity;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class SpotMarketDataMapper {
    
    public SpotMarketDataEntity toEntity(SpotMarketData dto, SymbolEntity symbol, ExchangeEntity exchange) {
        SpotMarketDataEntity entity = new SpotMarketDataEntity();
        entity.setSymbol(symbol);
        entity.setExchange(exchange);
        entity.setTimestamp(dto.getTimestamp());
        entity.setPrice(dto.getPrice());
        entity.setVolume(dto.getVolume());
        entity.setOpen(dto.getOpen());
        entity.setHigh(dto.getHigh());
        entity.setLow(dto.getLow());
        entity.setClose(dto.getClose());
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }

    public SpotMarketData toDto(SpotMarketDataEntity entity) {
        SpotMarketData dto = new SpotMarketData();
        dto.setSymbol(entity.getSymbol().getSymbol());
        dto.setExchange(entity.getExchange().getName());
        dto.setTimestamp(entity.getTimestamp());
        dto.setPrice(entity.getPrice());
        dto.setVolume(entity.getVolume());
        dto.setOpen(entity.getOpen());
        dto.setHigh(entity.getHigh());
        dto.setLow(entity.getLow());
        dto.setClose(entity.getClose());
        return dto;
    }
} 