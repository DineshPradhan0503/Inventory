package com.inventory.management.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SalesReport {
    private double totalSalesAmount;
    private long totalSalesCount;
}
