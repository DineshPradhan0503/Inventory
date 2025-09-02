package com.inventory.management.services;

import com.inventory.management.models.Product;
import com.inventory.management.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> searchProducts(
            String keyword,
            String category,
            Double minPrice,
            Double maxPrice,
            Integer stockLevelLessThan,
            Integer stockLevelGreaterThan,
            String sortBy,
            String sortDir,
            int page,
            int size
    ) {
        List<Criteria> criteriaList = new ArrayList<>();

        if (keyword != null && !keyword.isBlank()) {
            String regex = ".*" + keyword.trim() + ".*";
            criteriaList.add(new Criteria().orOperator(
                    Criteria.where("name").regex(regex, "i"),
                    Criteria.where("category").regex(regex, "i"),
                    Criteria.where("description").regex(regex, "i")
            ));
        }

        if (category != null && !category.isBlank()) {
            criteriaList.add(Criteria.where("category").is(category));
        }

        if (minPrice != null) {
            criteriaList.add(Criteria.where("price").gte(minPrice));
        }

        if (maxPrice != null) {
            criteriaList.add(Criteria.where("price").lte(maxPrice));
        }

        if (stockLevelLessThan != null) {
            criteriaList.add(Criteria.where("stockQuantity").lt(stockLevelLessThan));
        }

        if (stockLevelGreaterThan != null) {
            criteriaList.add(Criteria.where("stockQuantity").gt(stockLevelGreaterThan));
        }

        Query query = new Query();
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.isBlank()) {
            Sort.Direction direction = Objects.equals(sortDir, "desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            sort = Sort.by(direction, sortBy);
            query.with(sort);
        }

        long total = mongoTemplate.count(query, Product.class);

        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1));
        query.with(pageable);

        List<Product> products = mongoTemplate.find(query, Product.class);
        return new PageImpl<>(products, pageable, total);
    }

    public List<Product> getLowStockProducts() {
        Query query = new Query();
        query.addCriteria(Criteria.where("stockQuantity").lt("$threshold"));
        // The above expression does not compare fields directly in MongoTemplate. Implement in-memory:
        List<Product> all = productRepository.findAll();
        List<Product> low = new ArrayList<>();
        for (Product p : all) {
            if (p.getStockQuantity() < p.getThreshold()) {
                low.add(p);
            }
        }
        return low;
    }

    public Product increaseStock(String productId, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Increase amount must be positive");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
        product.setStockQuantity(product.getStockQuantity() + amount);
        return productRepository.save(product);
    }

    public Product decreaseStock(String productId, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Decrease amount must be positive");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
        if (product.getStockQuantity() - amount < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        product.setStockQuantity(product.getStockQuantity() - amount);
        return productRepository.save(product);
    }
}

