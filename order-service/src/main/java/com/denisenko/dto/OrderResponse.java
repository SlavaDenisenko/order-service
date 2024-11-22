package com.denisenko.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private boolean success;
    private Integer orderId;
    private Integer userId;
    private BigDecimal price;
    private String comment;
    private BigDecimal availableBalance;
}
