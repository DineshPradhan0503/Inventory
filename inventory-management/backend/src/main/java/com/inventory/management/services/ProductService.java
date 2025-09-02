package com.inventory.management.services;

import com.inventory.management.models.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product createProduct(Product product);
    List<Product> getAllProducts();
    Optional<Product> getProductById(String productId);
    Product updateProduct(String productId, Product productDetails);
    void deleteProduct(String productId);
}
