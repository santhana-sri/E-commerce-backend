package com.hex.ecommerce.controller;

import com.hex.ecommerce.dto.CategoryStatsDto;
import com.hex.ecommerce.dto.PagedResponse;
import com.hex.ecommerce.dto.ProductDto;
import com.hex.ecommerce.dto.ProductInfoDto;
import com.hex.ecommerce.enums.Category;
import com.hex.ecommerce.enums.SortDirection;
import com.hex.ecommerce.model.Product;
import com.hex.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getById(id);
        return ResponseEntity.ok(product);
    }

    // Filter API for products based on category (removed pagination as per requirement)
    @GetMapping("/filter")
    public ResponseEntity<List<ProductDto>> filterProductsByCategory(
            @RequestParam Category category) {
        List<ProductDto> products = productService.filterProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    // API to fetch and sort products in ASC/DESC order by price using Streams with pagination
    @GetMapping("/sort")
    public ResponseEntity<PagedResponse<ProductDto>> sortProductsByPrice(
            @RequestParam SortDirection direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<ProductDto> sortedProducts = productService.sortProductsByPrice(direction, page, size);
        return ResponseEntity.ok(sortedProducts);
    }

    // API using Streams to display number of products in each category (Task 3)
    @GetMapping("/category-stats")
    public ResponseEntity<List<CategoryStatsDto>> getCategoryStatistics() {
        List<CategoryStatsDto> stats = productService.getCategoryStatistics();
        return ResponseEntity.ok(stats);
    }

    // API to display product info with number of customers who purchased the product
    @GetMapping("/info-with-customer-count")
    public ResponseEntity<List<ProductInfoDto>> getProductInfoWithCustomerCount() {
        List<ProductInfoDto> productInfo = productService.getProductInfoWithCustomerCount();
        return ResponseEntity.ok(productInfo);
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<ProductDto>> getProductsByVendor(@PathVariable Long vendorId) {
        List<ProductDto> products = productService.getProductsByVendor(vendorId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<ProductDto>> getProductsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        List<ProductDto> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
}
