package com.inventory.management.services;

import com.inventory.management.exception.InsufficientStockException;
import com.inventory.management.exception.ResourceNotFoundException;
import com.inventory.management.models.Product;
import com.inventory.management.models.Sale;
import com.inventory.management.payload.request.SaleRequest;
import com.inventory.management.payload.response.SaleResponse;
import com.inventory.management.repository.ProductRepository;
import com.inventory.management.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public SaleResponse createSale(SaleRequest saleRequest, String userId) {
        Product product = productRepository.findById(saleRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + saleRequest.getProductId()));

        if (product.getStockQuantity() < saleRequest.getQuantitySold()) {
            throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
        }

        product.setStockQuantity(product.getStockQuantity() - saleRequest.getQuantitySold());
        productRepository.save(product);

        Sale sale = new Sale(
                saleRequest.getProductId(),
                saleRequest.getQuantitySold(),
                userId
        );
        Sale newSale = saleRepository.save(sale);

        return new SaleResponse(
                newSale.getId(),
                newSale.getProductId(),
                newSale.getQuantitySold(),
                newSale.getSaleDate(),
                newSale.getUserId()
        );
    }

    public List<SaleResponse> getAllSales() {
        return saleRepository.findAll().stream()
                .map(sale -> new SaleResponse(
                        sale.getId(),
                        sale.getProductId(),
                        sale.getQuantitySold(),
                        sale.getSaleDate(),
                        sale.getUserId()))
                .collect(Collectors.toList());
    }
}
