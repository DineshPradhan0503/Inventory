package com.inventory.management.controllers;

import com.inventory.management.payload.response.ApiResponse;
import com.inventory.management.payload.response.SalesReport;
import com.inventory.management.payload.response.StockReportItem;
import com.inventory.management.services.ReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/reports")
public class ReportingController {

    @Autowired
    private ReportingService reportingService;

    @GetMapping("/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<StockReportItem>>> getStockReport() {
        List<StockReportItem> report = reportingService.generateStockReport();
        return ResponseEntity.ok(new ApiResponse<>(true, "Stock report generated successfully", report));
    }

    @GetMapping("/sales")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SalesReport>> getSalesReport() {
        SalesReport report = reportingService.generateSalesReport();
        return ResponseEntity.ok(new ApiResponse<>(true, "Sales report generated successfully", report));
    }
}
