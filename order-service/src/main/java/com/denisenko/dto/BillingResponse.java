package com.denisenko.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingResponse {
    private boolean success;
    private BigDecimal remainingBalance;
    private String message;
}
