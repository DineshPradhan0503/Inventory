package com.inventory.management.models;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
@Data
public class Product {

    @Id
    private String id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String category;

    @Size(max = 250)
    private String description;

    @DecimalMin(value = "0.0", inclusive = false)
    private double price;

    @Min(0)
    private int stockQuantity;

    @Min(0)
    private int threshold; // Minimum stock before alert

    public Product(String name, String category, String description, double price, int stockQuantity, int threshold) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.threshold = threshold;
    }
}
