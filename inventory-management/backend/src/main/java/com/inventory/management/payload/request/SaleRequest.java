package com.inventory.management.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SaleRequest {

    @NotBlank
    private String productId;

    @Min(1)
    private int quantitySold;
}
