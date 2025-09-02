package com.inventory.management.controllers;

import com.inventory.management.payload.request.SaleRequest;
import com.inventory.management.payload.response.ApiResponse;
import com.inventory.management.payload.response.SaleResponse;
import com.inventory.management.security.services.UserDetailsImpl;
import com.inventory.management.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SaleResponse>> createSale(@Valid @RequestBody SaleRequest saleRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String userId = userDetails.getId();

        SaleResponse newSale = saleService.createSale(saleRequest, userId);

        return new ResponseEntity<>(new ApiResponse<>(true, "Sale created successfully", newSale), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<SaleResponse>>> getAllSales() {
        List<SaleResponse> sales = saleService.getAllSales();
        return ResponseEntity.ok(new ApiResponse<>(true, "Sales fetched successfully", sales));
    }
}
