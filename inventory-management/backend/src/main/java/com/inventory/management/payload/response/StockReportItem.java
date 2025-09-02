package com.inventory.management.payload.response;

import com.inventory.management.models.Product;
import lombok.Data;

@Data
public class StockReportItem {
    private String productId;
    private String name;
    private String category;
    private int stockQuantity;
    private int threshold;
    private boolean isLowStock;

    public StockReportItem(Product product) {
        this.productId = product.getId();
        this.name = product.getName();
        this.category = product.getCategory();
        this.stockQuantity = product.getStockQuantity();
        this.threshold = product.getThreshold();
        this.isLowStock = product.getStockQuantity() < product.getThreshold();
    }
}
