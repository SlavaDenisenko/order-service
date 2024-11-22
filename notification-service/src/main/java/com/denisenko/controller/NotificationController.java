package com.denisenko.controller;

import com.denisenko.model.Notification;
import com.denisenko.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Integer userId) {
        List<Notification> notifications = notificationService.getNotifications(userId);
        return notifications.isEmpty() ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).build() :
                ResponseEntity.ok(notifications);
    }
}
