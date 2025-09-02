package com.inventory.management.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "sales")
@Data
public class Sale {

    @Id
    private String id;

    @NotBlank
    private String productId;

    @Min(1)
    private int quantitySold;

    private Instant saleDate;

    @NotBlank
    private String userId;

    public Sale(String productId, int quantitySold, String userId) {
        this.productId = productId;
        this.quantitySold = quantitySold;
        this.userId = userId;
        this.saleDate = Instant.now();
    }
}
