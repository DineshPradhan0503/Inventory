package com.inventory.management.services;

import com.inventory.management.models.Product;
import com.inventory.management.models.Sale;
import com.inventory.management.payload.response.SalesReport;
import com.inventory.management.payload.response.StockReportItem;
import com.inventory.management.repository.ProductRepository;
import com.inventory.management.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Comparator;

@Service
public class ReportingService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    public SaleRepository saleRepository;

    public List<StockReportItem> generateStockReport() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(StockReportItem::new)
                .collect(Collectors.toList());
    }

    public SalesReport generateSalesReport() {
        List<Sale> sales = saleRepository.findAll();
        long totalSalesCount = sales.size();
        double totalSalesAmount = sales.stream()
                .mapToDouble(sale -> {
                    Product product = productRepository.findById(sale.getProductId()).orElse(null);
                    return product != null ? product.getPrice() * sale.getQuantitySold() : 0;
                })
                .sum();

        return new SalesReport(totalSalesAmount, totalSalesCount);
    }

    public List<Map.Entry<String, Long>> getBestSellingProducts(int topN) {
        List<Sale> sales = saleRepository.findAll();
        Map<String, Long> counts = sales.stream()
                .collect(Collectors.groupingBy(Sale::getProductId, Collectors.summingLong(Sale::getQuantitySold)));
        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .limit(topN)
                .collect(Collectors.toList());
    }
}
