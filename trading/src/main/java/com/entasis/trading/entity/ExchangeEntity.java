package com.entasis.trading.entity;

import com.entasis.trading.entity.enums.ExchangeStatus;
import com.entasis.trading.entity.enums.ExchangeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "exchanges")
@Getter
@Setter
public class ExchangeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ExchangeType type;
    
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ExchangeStatus status;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
} 