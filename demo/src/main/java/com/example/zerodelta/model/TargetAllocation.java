package com.example.zerodelta.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.annotation.EnumNaming;

import java.math.BigDecimal;

@Entity
@Table(name="target_allocations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class TargetAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetClass assetClass;


    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal targetPercentage;
}
