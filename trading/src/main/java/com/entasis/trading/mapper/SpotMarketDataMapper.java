package com.entasis.trading.mapper;

import com.entasis.trading.dto.SpotMarketData;
import com.entasis.trading.entity.SpotMarketDataEntity;
import com.entasis.trading.entity.Symbol;
import org.springframework.stereotype.Component;

@Component
public class SpotMarketDataMapper extends BaseMarketDataMapper<SpotMarketDataEntity, SpotMarketData> {
    
    public SpotMarketDataEntity toEntity(SpotMarketData dto, Symbol symbol) {
        SpotMarketDataEntity entity = new SpotMarketDataEntity();
        mapCommonEntityFields(dto, entity, symbol);
        entity.setOpen(dto.getOpen());
        entity.setHigh(dto.getHigh());
        entity.setLow(dto.getLow());
        entity.setClose(dto.getClose());
        return entity;
    }

    public SpotMarketData toDto(SpotMarketDataEntity entity) {
        SpotMarketData dto = new SpotMarketData();
        mapCommonDtoFields(entity, dto);
        dto.setOpen(entity.getOpen());
        dto.setHigh(entity.getHigh());
        dto.setLow(entity.getLow());
        dto.setClose(entity.getClose());
        return dto;
    }
} 