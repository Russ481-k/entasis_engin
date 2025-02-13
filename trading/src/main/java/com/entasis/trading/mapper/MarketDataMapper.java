package com.entasis.trading.mapper;

import com.entasis.trading.dto.MarketData;
import com.entasis.trading.entity.MarketDataEntity;
import org.springframework.stereotype.Component;

@Component
public class MarketDataMapper {
    
    public MarketData toDto(MarketDataEntity entity) {
        return MarketData.builder()
            .id(entity.getId())
            .symbol(entity.getSymbol())
            .exchange(entity.getExchange())
            .timestamp(entity.getTimestamp())
            .price(entity.getPrice())
            .volume(entity.getVolume())
            .open(entity.getOpen())
            .high(entity.getHigh())
            .low(entity.getLow())
            .close(entity.getClose())
            .createdAt(entity.getCreatedAt())
            .build();
    }
    
    public MarketDataEntity toEntity(MarketData dto) {
        return MarketDataEntity.builder()
            .id(dto.getId())
            .symbol(dto.getSymbol())
            .exchange(dto.getExchange())
            .timestamp(dto.getTimestamp())
            .price(dto.getPrice())
            .volume(dto.getVolume())
            .open(dto.getOpen())
            .high(dto.getHigh())
            .low(dto.getLow())
            .close(dto.getClose())
            .createdAt(dto.getCreatedAt())
            .build();
    }
} 