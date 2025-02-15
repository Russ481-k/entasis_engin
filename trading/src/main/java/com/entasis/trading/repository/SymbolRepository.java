package com.entasis.trading.repository;

import com.entasis.trading.entity.Symbol;
import com.entasis.trading.entity.enums.ExchangeType;
import com.entasis.trading.entity.enums.InstrumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface SymbolRepository extends JpaRepository<Symbol, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Symbol s WHERE s.exchangeSymbol = :symbol " +
           "AND s.instrumentType = :type AND s.exchange.type = :exchangeType " +
           "ORDER BY s.id DESC LIMIT 1")
    Optional<Symbol> findByExchangeSymbolAndInstrumentTypeAndExchange_Type(
        String symbol,
        InstrumentType type,
        ExchangeType exchangeType
    );
} 