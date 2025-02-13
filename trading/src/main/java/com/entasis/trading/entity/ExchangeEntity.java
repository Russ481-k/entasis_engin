package com.entasis.trading.entity;

import com.entasis.trading.entity.enums.ExchangeStatus;
import com.entasis.trading.entity.enums.ExchangeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "exchanges")
@Getter @Setter
public class ExchangeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ExchangeType type;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ExchangeStatus status;
    
    private LocalDateTime createdAt;
} 