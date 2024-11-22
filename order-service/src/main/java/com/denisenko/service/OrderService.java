package com.denisenko.service;

import com.denisenko.client.BillingClient;
import com.denisenko.dto.BillingRequest;
import com.denisenko.dto.BillingResponse;
import com.denisenko.dto.OrderRequest;
import com.denisenko.dto.OrderResponse;
import com.denisenko.event.OrderEvent;
import com.denisenko.model.Order;
import com.denisenko.model.User;
import com.denisenko.repository.OrderRepository;
import com.denisenko.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BillingClient billingClient;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Value("${spring.kafka.topics.order-placement}")
    private String orderNotificationTopic;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        BillingRequest billingRequest = BillingRequest.builder().amount(orderRequest.getPrice()).build();
        BillingResponse billingResponse = billingClient.debit(orderRequest.getUserId(), billingRequest);
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setUserId(orderRequest.getUserId());
        orderEvent.setPrice(orderRequest.getPrice());
        if (!billingResponse.isSuccess()) {
            orderEvent.setStatus("FAIL");
            orderEvent.setMessage(billingResponse.getMessage());
            kafkaTemplate.send(orderNotificationTopic, orderEvent);
            return OrderResponse.builder()
                    .success(false)
                    .price(orderRequest.getPrice())
                    .comment(billingResponse.getMessage())
                    .availableBalance(billingResponse.getRemainingBalance())
                    .build();
        }
        Order order = mapToEntity(orderRequest);
        orderRepository.save(order);
        orderEvent.setOrderId(order.getId());
        orderEvent.setStatus("SUCCESS");
        orderEvent.setMessage("Order placed successfully");
        kafkaTemplate.send(orderNotificationTopic, orderEvent);
        return mapToDto(order);
    }

    private Order mapToEntity(OrderRequest orderRequest) {
        User user = userRepository.findById(orderRequest.getUserId()).orElse(null);
        return Order.builder()
                .user(user)
                .price(orderRequest.getPrice())
                .comment(orderRequest.getComment())
                .build();
    }

    private OrderResponse mapToDto(Order order) {
        return OrderResponse.builder()
                .success(true)
                .orderId(order.getId())
                .userId(order.getUser().getId())
                .price(order.getPrice())
                .comment(order.getComment())
                .build();
    }
}
