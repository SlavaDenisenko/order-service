package com.denisenko.service;

import com.denisenko.event.OrderEvent;
import com.denisenko.model.Notification;
import com.denisenko.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "${spring.kafka.topics.order-placement}", groupId = "${spring.kafka.consumer.group-id}")
    public void saveNotification(OrderEvent orderEvent) {
        Notification notification = mapToEntity(orderEvent);
        notificationRepository.save(notification);
        log.info("Notification was saved. User ID={}, status={}", notification.getUserId(), notification.getStatus());
    }

    public List<Notification> getNotifications(Integer userId) {
        return notificationRepository.findAllByUserId(userId);
    }

    private Notification mapToEntity(OrderEvent orderEvent) {
        return Notification.builder()
                .orderId(orderEvent.getOrderId())
                .userId(orderEvent.getUserId())
                .status(orderEvent.getStatus())
                .price(orderEvent.getPrice())
                .message(orderEvent.getMessage())
                .build();
    }
}
