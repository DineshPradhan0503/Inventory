package com.inventory.management.services;

import com.inventory.management.models.Product;
import com.inventory.management.models.Sale;
import com.inventory.management.payload.request.SaleRequest;
import com.inventory.management.repository.ProductRepository;
import com.inventory.management.repository.SaleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {com.inventory.management.InventoryManagementApplication.class, com.inventory.management.TestMailConfig.class})
class SaleServiceTests {

    @Autowired
    SaleService saleService;

    @MockBean
    SaleRepository saleRepository;

    @MockBean
    ProductRepository productRepository;

    @Test
    void createSale_shouldThrowWhenInsufficientStock() {
        Product product = new Product("Pen", "Stationery", "Blue", 1.5, 2, 1);
        product.setId("p1");
        when(productRepository.findById("p1")).thenReturn(Optional.of(product));

        SaleRequest req = new SaleRequest();
        req.setProductId("p1");
        req.setQuantitySold(5);

        Assertions.assertThrows(RuntimeException.class, () -> saleService.createSale(req, "u1"));
    }

    @Test
    void createSale_shouldDecreaseStock() {
        Product product = new Product("Pen", "Stationery", "Blue", 1.5, 10, 1);
        product.setId("p1");
        when(productRepository.findById("p1")).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));
        when(saleRepository.save(any(Sale.class))).thenAnswer(i -> i.getArgument(0));

        SaleRequest req = new SaleRequest();
        req.setProductId("p1");
        req.setQuantitySold(3);

        Sale sale = saleService.createSale(req, "u1");
        Assertions.assertNotNull(sale);
        Assertions.assertEquals(7, product.getStockQuantity());
    }
}

