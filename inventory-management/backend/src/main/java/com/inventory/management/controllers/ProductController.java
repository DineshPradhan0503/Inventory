package com.inventory.management.controllers;

import com.inventory.management.models.Product;
import com.inventory.management.services.ProductService;
import com.inventory.management.exception.ResourceNotFoundException;
import com.inventory.management.payload.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Product>> createProduct(@Valid @RequestBody Product product) {
        Product savedProduct = productService.createProduct(product);
        return new ResponseEntity<>(new ApiResponse<>(true, "Product created successfully", savedProduct), HttpStatus.CREATED);
    }

    @GetMapping("/products")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse<>(true, "Products fetched successfully", products));
    }

    @GetMapping("/products/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable(value = "id") String productId) {
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        return ResponseEntity.ok(new ApiResponse<>(true, "Product fetched successfully", product));
    }

    @PutMapping("/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable(value = "id") String productId,
                                                 @Valid @RequestBody Product productDetails) {
        Product updatedProduct = productService.updateProduct(productId, productDetails);
        return ResponseEntity.ok(new ApiResponse<>(true, "Product updated successfully", updatedProduct));
    }

    @DeleteMapping("/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable(value = "id") String productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Product deleted successfully"));
    }
}
