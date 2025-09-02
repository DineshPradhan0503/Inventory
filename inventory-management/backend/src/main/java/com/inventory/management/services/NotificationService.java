package com.inventory.management.services;

import com.inventory.management.models.Product;
import com.inventory.management.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmailService emailService;

    @Value("${alerts.mail.to:}")
    private String alertsRecipient;

    public int sendLowStockAlerts() {
        List<Product> low = productRepository.findAll().stream()
                .filter(p -> p.getStockQuantity() < p.getThreshold())
                .collect(Collectors.toList());
        if (alertsRecipient == null || alertsRecipient.isBlank()) {
            return low.size();
        }
        for (Product p : low) {
            String subject = "Low stock: " + p.getName();
            String text = String.format("Product %s (ID: %s) is low on stock. Current: %d, Threshold: %d",
                    p.getName(), p.getId(), p.getStockQuantity(), p.getThreshold());
            emailService.sendSimple(alertsRecipient, subject, text);
        }
        return low.size();
    }
}

