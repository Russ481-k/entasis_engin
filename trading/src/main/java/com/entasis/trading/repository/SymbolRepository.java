package com.entasis.trading.repository;

import com.entasis.trading.entity.SymbolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SymbolRepository extends JpaRepository<SymbolEntity, Long> {
    Optional<SymbolEntity> findBySymbol(String symbol);
} 