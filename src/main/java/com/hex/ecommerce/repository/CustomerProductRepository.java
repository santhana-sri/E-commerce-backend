package com.hex.ecommerce.repository;

import com.hex.ecommerce.model.CustomerProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerProductRepository extends JpaRepository<CustomerProduct, Long> {
    
    List<CustomerProduct> findByCustomerId(Long customerId);
    
    List<CustomerProduct> findByProductId(Long productId);
    
    @Query("SELECT cp FROM CustomerProduct cp WHERE cp.customer.id = :customerId AND cp.product.id = :productId")
    List<CustomerProduct> findByCustomerIdAndProductId(@Param("customerId") Long customerId, @Param("productId") Long productId);
}
