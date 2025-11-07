package com.hex.ecommerce.repository;

import com.hex.ecommerce.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    List<Customer> findByCity(String city);
    
    @Query("SELECT c FROM Customer c JOIN c.customerProducts cp GROUP BY c.id ORDER BY COUNT(cp) DESC")
    List<Customer> findCustomersOrderByPurchaseCount();
}
