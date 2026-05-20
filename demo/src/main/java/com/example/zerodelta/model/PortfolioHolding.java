package com.example.zerodelta.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name="portfolio_holding")


public class PortfolioHolding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="asset_id",nullable = false)
    private Asset asset;

    @Column(nullable = false, precision = 19,scale = 8)
    private BigDecimal units;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal averageBuyPrice;
}
