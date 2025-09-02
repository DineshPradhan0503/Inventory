package com.inventory.management.payload.response;

import lombok.Data;

import java.time.Instant;

@Data
public class SaleResponse {

    private String id;
    private String productId;
    private int quantitySold;
    private Instant saleDate;
    private String userId;

    public SaleResponse(String id, String productId, int quantitySold, Instant saleDate, String userId) {
        this.id = id;
        this.productId = productId;
        this.quantitySold = quantitySold;
        this.saleDate = saleDate;
        this.userId = userId;
    }
}
