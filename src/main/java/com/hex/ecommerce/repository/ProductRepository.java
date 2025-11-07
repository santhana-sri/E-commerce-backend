package com.hex.ecommerce.repository;

import com.hex.ecommerce.enums.Category;
import com.hex.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Page<Product> findByCategory(Category category, Pageable pageable);
    
    List<Product> findByVendorId(Long vendorId);
    
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
    
    @Query("SELECT p FROM Product p WHERE p.stockQuantity > 0")
    List<Product> findAvailableProducts();
    
    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.stockQuantity > 0")
    Page<Product> findAvailableProductsByCategory(@Param("category") Category category, Pageable pageable);
}
