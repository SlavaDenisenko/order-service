package com.denisenko.controller;

import com.denisenko.dto.OrderErrorResponse;
import com.denisenko.dto.OrderRequest;
import com.denisenko.dto.OrderResponse;
import com.denisenko.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.placeOrder(orderRequest);
        if (!orderResponse.isSuccess()) {
            OrderErrorResponse errorResponse = OrderErrorResponse.builder()
                    .error(orderResponse.getComment())
                    .userId(orderRequest.getUserId())
                    .requiredAmount(orderRequest.getPrice())
                    .availableAmount(orderResponse.getAvailableBalance())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }
}
