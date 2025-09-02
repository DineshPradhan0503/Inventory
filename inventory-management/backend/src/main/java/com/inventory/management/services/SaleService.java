package com.inventory.management.services;

import com.inventory.management.models.Product;
import com.inventory.management.models.Sale;
import com.inventory.management.payload.request.SaleRequest;
import com.inventory.management.repository.ProductRepository;
import com.inventory.management.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Sale createSale(SaleRequest saleRequest, String userId) {
        // 1. Find the product
        Product product = productRepository.findById(saleRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + saleRequest.getProductId()));

        // 2. Check if there is enough stock
        if (product.getStockQuantity() < saleRequest.getQuantitySold()) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }

        // 3. Decrease the stock quantity
        product.setStockQuantity(product.getStockQuantity() - saleRequest.getQuantitySold());
        productRepository.save(product);

        // 4. Create and save the sale
        Sale sale = new Sale(
                saleRequest.getProductId(),
                saleRequest.getQuantitySold(),
                userId
        );
        return saleRepository.save(sale);
    }

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }
}
