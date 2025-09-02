package com.inventory.management.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.management.models.Product;
import com.inventory.management.repository.ProductRepository;
import com.inventory.management.services.ProductService;
import com.inventory.management.repository.AuditLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {com.inventory.management.InventoryManagementApplication.class, com.inventory.management.TestMailConfig.class})
@AutoConfigureMockMvc
class ProductControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    ProductService productService;

    @MockBean
    AuditLogRepository auditLogRepository;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void createProduct_shouldReturn201() throws Exception {
        Product payload = new Product("Pen", "Stationery", "Blue pen", 1.5, 100, 10);
        Product saved = new Product("Pen", "Stationery", "Blue pen", 1.5, 100, 10);
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void getProducts_shouldReturn200() throws Exception {
        when(productRepository.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateProduct_shouldReturn404WhenMissing() throws Exception {
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());
        Product payload = new Product("Pen", "Stationery", "Blue pen", 1.5, 100, 10);
        mockMvc.perform(put("/api/products/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void searchProducts_shouldReturn200() throws Exception {
        Page<Product> page = new PageImpl<>(List.of());
        when(productService.searchProducts(any(), any(), any(), any(), any(), any(), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(page);
        mockMvc.perform(get("/api/products/search").param("q", "Pen"))
                .andExpect(status().isOk());
    }
}

