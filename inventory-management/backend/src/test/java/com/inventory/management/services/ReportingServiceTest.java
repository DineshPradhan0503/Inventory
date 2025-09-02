package com.inventory.management.services;

import com.inventory.management.models.Product;
import com.inventory.management.models.Sale;
import com.inventory.management.payload.response.SalesReport;
import com.inventory.management.payload.response.StockReportItem;
import com.inventory.management.repository.ProductRepository;
import com.inventory.management.repository.SaleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportingServiceTest {

    @InjectMocks
    private ReportingService reportingService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SaleRepository saleRepository;

    @Test
    public void testGenerateStockReport() {
        // Arrange
        Product p1 = new Product("P1", "C1", "D1", 10.0, 5, 10);
        Product p2 = new Product("P2", "C2", "D2", 20.0, 20, 10);
        p1.setId("1");
        p2.setId("2");
        List<Product> products = Arrays.asList(p1, p2);
        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<StockReportItem> report = reportingService.generateStockReport();

        // Assert
        assertEquals(2, report.size());
        assertTrue(report.get(0).isLowStock());
        assertEquals(5, report.get(0).getStockQuantity());
    }

    @Test
    public void testGenerateSalesReport() {
        // Arrange
        Product p1 = new Product("P1", "C1", "D1", 10.0, 5, 10);
        p1.setId("1");
        Sale s1 = new Sale("1", 2, "user1");
        Sale s2 = new Sale("1", 3, "user2");
        List<Sale> sales = Arrays.asList(s1, s2);

        when(saleRepository.findAll()).thenReturn(sales);
        when(productRepository.findById("1")).thenReturn(Optional.of(p1));

        // Act
        SalesReport report = reportingService.generateSalesReport();

        // Assert
        assertEquals(2, report.getTotalSalesCount());
        assertEquals(50.0, report.getTotalSalesAmount());
    }
}
