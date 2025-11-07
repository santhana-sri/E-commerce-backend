package com.hex.ecommerce.service;

import com.hex.ecommerce.dto.CategoryStatsDto;
import com.hex.ecommerce.dto.PagedResponse;
import com.hex.ecommerce.dto.ProductDto;
import com.hex.ecommerce.dto.ProductInfoDto;
import com.hex.ecommerce.enums.Category;
import com.hex.ecommerce.enums.SortDirection;
import com.hex.ecommerce.exception.InvalidIdException;
import com.hex.ecommerce.model.Product;
import com.hex.ecommerce.model.Vendor;
import com.hex.ecommerce.repository.ProductRepository;
import com.hex.ecommerce.repository.VendorRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;

    public Product getById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new InvalidIdException("Product Id Invalid: " + productId));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product saveProduct(Product product) {
        // If vendor is provided with only ID, load the complete vendor entity
        if (product.getVendor() != null && product.getVendor().getId() != null) {
            Vendor vendor = vendorRepository.findById(product.getVendor().getId())
                    .orElseThrow(() -> new InvalidIdException("Vendor Id Invalid: " + product.getVendor().getId()));
            product.setVendor(vendor);
        }
        return productRepository.save(product);
    }

    // Filter products by category (removed pagination as per requirement)
    public List<ProductDto> filterProductsByCategory(Category category) {
        List<Product> products = productRepository.findAvailableProducts().stream()
                .filter(p -> p.getCategory().equals(category))
                .collect(Collectors.toList());
        
        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Sort products by price using Streams with pagination (as per requirement)
    public PagedResponse<ProductDto> sortProductsByPrice(SortDirection sortDirection, int page, int size) {
        List<Product> products = productRepository.findAvailableProducts();
        
        List<Product> sortedProducts = switch (sortDirection) {
            case ASC -> products.stream()
                    .sorted((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()))
                    .collect(Collectors.toList());
            case DESC -> products.stream()
                    .sorted((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()))
                    .collect(Collectors.toList());
        };
        
        // Manual pagination
        int start = page * size;
        int end = Math.min(start + size, sortedProducts.size());
        List<Product> paginatedProducts = sortedProducts.subList(start, end);
        
        List<ProductDto> productDtos = paginatedProducts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        
        // Calculate pagination info
        int totalPages = (int) Math.ceil((double) sortedProducts.size() / size);
        
        return new PagedResponse<>(
                productDtos,
                page,
                size,
                sortedProducts.size(),
                totalPages,
                page == 0,
                page == totalPages - 1
        );
    }

    // Get category statistics using Streams (as per requirement)
    public List<CategoryStatsDto> getCategoryStatistics() {
        List<Product> products = productRepository.findAll();
        
        Map<Category, Long> categoryStats = products.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()));
        
        return categoryStats.entrySet().stream()
                .map(entry -> new CategoryStatsDto(entry.getKey(), entry.getValue()))
                .sorted((s1, s2) -> Long.compare(s2.getProductCount(), s1.getProductCount()))
                .collect(Collectors.toList());
    }

    // Get products by vendor
    public List<ProductDto> getProductsByVendor(Long vendorId) {
        List<Product> products = productRepository.findByVendorId(vendorId);
        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Search products by price range
    public List<ProductDto> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        List<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice);
        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Get product info with customer count using Streams
    public List<ProductInfoDto> getProductInfoWithCustomerCount() {
        List<Product> products = productRepository.findAll();
        
        return products.stream()
                .map(product -> {
                    // Count unique customers who purchased this product using Streams
                    long customerCount = product.getCustomerProducts() != null ? 
                        product.getCustomerProducts().stream()
                            .map(cp -> cp.getCustomer().getId())
                            .distinct()
                            .count() : 0;
                    
                    return new ProductInfoDto(
                            product.getId(),
                            product.getTitle(),
                            product.getDescription(),
                            product.getCategory(),
                            product.getPrice(),
                            product.getStockQuantity(),
                            product.getVendor().getName(),
                            product.getVendor().getId(),
                            customerCount
                    );
                })
                .collect(Collectors.toList());
    }

    // Convert Product entity to DTO
    private ProductDto convertToDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getCategory(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getVendor().getName(),
                product.getVendor().getId()
        );
    }
}
