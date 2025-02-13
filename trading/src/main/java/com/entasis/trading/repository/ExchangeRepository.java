package com.entasis.trading.repository;

import com.entasis.trading.entity.ExchangeEntity;
import com.entasis.trading.entity.enums.ExchangeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExchangeRepository extends JpaRepository<ExchangeEntity, Long> {
    Optional<ExchangeEntity> findByName(String name);
    Optional<ExchangeEntity> findByNameAndType(String name, ExchangeType type);
} 