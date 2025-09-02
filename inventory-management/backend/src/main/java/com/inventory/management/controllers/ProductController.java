package com.inventory.management.controllers;

import com.inventory.management.models.Product;
import com.inventory.management.repository.ProductRepository;
import com.inventory.management.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.inventory.management.security.audit.Audited;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    // Create a new product
    @PostMapping("/products")
    @PreAuthorize("hasRole('ADMIN')")
    @Audited(action = "CREATE_PRODUCT", resource = "PRODUCT")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product savedProduct = productRepository.save(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    // Get all products
    @GetMapping("/products")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Search, filter, sort, paginate products
    @GetMapping("/products/search")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Page<Product> searchProducts(
            @RequestParam(name = "q", required = false) String keyword,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "minPrice", required = false) Double minPrice,
            @RequestParam(name = "maxPrice", required = false) Double maxPrice,
            @RequestParam(name = "stockLt", required = false) Integer stockLt,
            @RequestParam(name = "stockGt", required = false) Integer stockGt,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size
    ) {
        return productService.searchProducts(keyword, category, minPrice, maxPrice, stockLt, stockGt, sortBy, sortDir, page, size);
    }

    // Low stock list
    @GetMapping("/products/low-stock")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Product> getLowStockProducts() {
        return productService.getLowStockProducts();
    }

    // Increase stock
    @PostMapping("/products/{id}/stock/increase")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> increaseStock(@PathVariable("id") String id, @RequestParam("amount") int amount) {
        return ResponseEntity.ok(productService.increaseStock(id, amount));
    }

    // Decrease stock
    @PostMapping("/products/{id}/stock/decrease")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> decreaseStock(@PathVariable("id") String id, @RequestParam("amount") int amount) {
        return ResponseEntity.ok(productService.decreaseStock(id, amount));
    }

    // Get a single product by ID
    @GetMapping("/products/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Product> getProductById(@PathVariable(value = "id") String productId) {
        Optional<Product> product = productRepository.findById(productId);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update a product
    @PutMapping("/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Audited(action = "UPDATE_PRODUCT", resource = "PRODUCT")
    public ResponseEntity<Product> updateProduct(@PathVariable(value = "id") String productId,
                                                 @Valid @RequestBody Product productDetails) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(productDetails.getName());
            product.setCategory(productDetails.getCategory());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            product.setStockQuantity(productDetails.getStockQuantity());
            product.setThreshold(productDetails.getThreshold());
            Product updatedProduct = productRepository.save(product);
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a product
    @DeleteMapping("/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Audited(action = "DELETE_PRODUCT", resource = "PRODUCT")
    public ResponseEntity<Void> deleteProduct(@PathVariable(value = "id") String productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            productRepository.deleteById(productId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
