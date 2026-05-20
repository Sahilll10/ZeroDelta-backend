package com.example.zerodelta.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name="wallets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JoinColumn(name = "User_id", referencedColumnName = "id",nullable = false)
    private User user;

    @Column(nullable = false, precision = 19, scale=4)
    private BigDecimal fiatBalance;

    @Column(nullable = false)
    private String currency;
}
