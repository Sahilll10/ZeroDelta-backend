package com.example.zerodelta.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_orders", indexes={
        @Index(name = "idx_idempotency_key", columnList = "idempotency_key", unique=true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TransactionOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idempotency_key", nullable = false, unique = true, length = 100)
    private String idempotencyKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id")
    private Asset asset;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(precision = 19, scale = 4)
    private BigDecimal fiatAmount;

    @Column(precision = 19, scale=8)
    private BigDecimal assetUnits;

    @Column(precision = 19, scale = 4)
    private BigDecimal executionPrice;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt= LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate(){
        this.updatedAt= LocalDateTime.now();
    }
}
