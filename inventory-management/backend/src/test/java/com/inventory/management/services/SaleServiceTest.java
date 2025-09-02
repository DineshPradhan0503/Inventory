package com.inventory.management.services;

import com.inventory.management.exception.InsufficientStockException;
import com.inventory.management.models.Product;
import com.inventory.management.models.Sale;
import com.inventory.management.payload.request.SaleRequest;
import com.inventory.management.payload.response.SaleResponse;
import com.inventory.management.repository.ProductRepository;
import com.inventory.management.repository.SaleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SaleServiceTest {

    @InjectMocks
    private SaleService saleService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SaleRepository saleRepository;

    @Test
    public void testCreateSale_Success() {
        // Arrange
        Product product = new Product("Test Product", "Category", "Description", 10.0, 100, 10);
        product.setId("1");
        SaleRequest saleRequest = new SaleRequest();
        saleRequest.setProductId("1");
        saleRequest.setQuantitySold(5);

        Sale sale = new Sale("1", 5, "user1");
        sale.setId("sale1");

        when(productRepository.findById("1")).thenReturn(Optional.of(product));
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);

        // Act
        SaleResponse createdSaleResponse = saleService.createSale(saleRequest, "user1");

        // Assert
        assertNotNull(createdSaleResponse);
        assertEquals(95, product.getStockQuantity());
        assertEquals("1", createdSaleResponse.getProductId());
        assertEquals(5, createdSaleResponse.getQuantitySold());
        assertEquals("user1", createdSaleResponse.getUserId());
    }

    @Test
    public void testCreateSale_InsufficientStock() {
        // Arrange
        Product product = new Product("Test Product", "Category", "Description", 10.0, 5, 10);
        product.setId("1");
        SaleRequest saleRequest = new SaleRequest();
        saleRequest.setProductId("1");
        saleRequest.setQuantitySold(10);

        when(productRepository.findById("1")).thenReturn(Optional.of(product));

        // Act & Assert
        assertThrows(InsufficientStockException.class, () -> {
            saleService.createSale(saleRequest, "user1");
        });
    }
}
