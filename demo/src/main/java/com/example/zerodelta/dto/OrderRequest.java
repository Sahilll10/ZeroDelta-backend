package com.example.zerodelta.dto;

import com.example.zerodelta.model.OrderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRequest {

    @NotBlank(message = "Idempotency Keu is required to prevent duplicate charges")
    private String idempotencyKey;

    @NotNull(message = "User iD must be provided")
    private Long userId;

    private String ticker;

    @NotNull(message = "Order type is required")
    private OrderType orderType;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be strictly greaterr than Zero")
    private BigDecimal amount;
}
