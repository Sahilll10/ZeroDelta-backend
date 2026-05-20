package com.example.zerodelta.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "assets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String ticker;

    @Column(nullable = false)
    private String name;

    //IMP HAI TO MAP THE ENUM TO A STRING IN THE DATABASE
    @Enumerated (EnumType.STRING)
    @Column(nullable = false)
    private AssetClass assetClass;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal currentPrice;

    @Column(nullable = false)
    private boolean isTradable=true;
}
