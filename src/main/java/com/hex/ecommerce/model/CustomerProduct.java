package com.hex.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_product")
@Getter
@Setter
@NoArgsConstructor
public class CustomerProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnoreProperties({"customerProducts"})
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties({"customerProducts"})
    private Product product;

    @Column(name = "date_of_purchase")
    private LocalDateTime dateOfPurchase;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "total_cost")
    private Double totalCost;

    public CustomerProduct(Customer customer, Product product, Integer quantity, Double totalCost) {
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
        this.totalCost = totalCost;
        this.dateOfPurchase = LocalDateTime.now();
    }
}
