package com.inventory.management.controllers;

import com.inventory.management.payload.response.SalesReport;
import com.inventory.management.payload.response.StockReportItem;
import com.inventory.management.services.ReportingService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;
import com.inventory.management.services.ExportService;
import com.inventory.management.models.Sale;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/reports")
public class ReportingController {

    @Autowired
    private ReportingService reportingService;

    @Autowired
    private ExportService exportService;

    @GetMapping("/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StockReportItem>> getStockReport() {
        List<StockReportItem> report = reportingService.generateStockReport();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/sales")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SalesReport> getSalesReport() {
        SalesReport report = reportingService.generateSalesReport();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/best-sellers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map.Entry<String, Long>>> getBestSellers() {
        return ResponseEntity.ok(reportingService.getBestSellingProducts(10));
    }

    @GetMapping(value = "/stock/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportStockExcel() {
        List<StockReportItem> items = reportingService.generateStockReport();
        byte[] data = exportService.exportStockExcel(items);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stock_report.xlsx");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(data);
    }

    @GetMapping(value = "/sales/export", produces = MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportSalesPdf() {
        List<Sale> sales = reportingService.saleRepository.findAll();
        // Access via service: better expose method, but for brevity use repository through service addition later
        byte[] data = exportService.exportSalesPdf(sales);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=sales_report.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);
    }

    @GetMapping(value = "/stock/export/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportStockPdf() {
        List<StockReportItem> items = reportingService.generateStockReport();
        byte[] data = exportService.exportStockPdf(items);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=stock_report.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);
    }

    @GetMapping(value = "/sales/export/xlsx", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportSalesExcel() {
        List<Sale> sales = reportingService.saleRepository.findAll();
        byte[] data = exportService.exportSalesExcel(sales);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sales_report.xlsx");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(data);
    }
}
