package com.denisenko.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private Integer orderId;
    private Integer userId;
    private String status;
    private BigDecimal price;
    private String message;
}
