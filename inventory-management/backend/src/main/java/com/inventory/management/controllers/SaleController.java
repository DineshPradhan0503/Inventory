package com.inventory.management.controllers;

import com.inventory.management.models.Sale;
import com.inventory.management.payload.request.SaleRequest;
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
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<SaleResponse> createSale(@Valid @RequestBody SaleRequest saleRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String userId = userDetails.getId();

        Sale newSale = saleService.createSale(saleRequest, userId);

        SaleResponse saleResponse = new SaleResponse(
                newSale.getId(),
                newSale.getProductId(),
                newSale.getQuantitySold(),
                newSale.getSaleDate(),
                newSale.getUserId()
        );
        return new ResponseEntity<>(saleResponse, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SaleResponse>> getAllSales() {
        List<Sale> sales = saleService.getAllSales();
        List<SaleResponse> saleResponses = sales.stream()
                .map(sale -> new SaleResponse(
                        sale.getId(),
                        sale.getProductId(),
                        sale.getQuantitySold(),
                        sale.getSaleDate(),
                        sale.getUserId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(saleResponses);
    }
}
