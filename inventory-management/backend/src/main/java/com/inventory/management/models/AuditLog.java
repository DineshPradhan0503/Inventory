package com.inventory.management.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "audit_logs")
@Data
public class AuditLog {
    @Id
    private String id;
    private String userId;
    private String action;
    private String resource;
    private String resourceId;
    private Instant timestamp = Instant.now();
    private String details;
}

