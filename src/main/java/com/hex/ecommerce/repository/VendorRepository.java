package com.hex.ecommerce.repository;

import com.hex.ecommerce.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    
    List<Vendor> findByCity(String city);
    
    @Query("SELECT v FROM Vendor v JOIN v.products p GROUP BY v.id ORDER BY COUNT(p) DESC")
    List<Vendor> findVendorsOrderByProductCount();
}
