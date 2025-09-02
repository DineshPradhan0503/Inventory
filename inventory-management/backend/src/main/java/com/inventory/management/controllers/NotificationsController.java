package com.inventory.management.controllers;

import com.inventory.management.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/notifications")
public class NotificationsController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send-low-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> sendLowStockAlerts() {
        int count = notificationService.sendLowStockAlerts();
        return ResponseEntity.ok(Map.of("alertsSent", count));
    }
}

