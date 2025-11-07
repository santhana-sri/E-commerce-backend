package com.hex.ecommerce.controller;

import com.hex.ecommerce.dto.PurchaseRequestDto;
import com.hex.ecommerce.model.CustomerProduct;
import com.hex.ecommerce.service.PurchaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@AllArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    // POST API for customer to purchase products
    @PostMapping
    public ResponseEntity<CustomerProduct> purchaseProduct(@RequestBody PurchaseRequestDto purchaseRequest) {
        CustomerProduct purchase = purchaseService.purchaseProduct(purchaseRequest);
        return new ResponseEntity<>(purchase, HttpStatus.CREATED);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerProduct>> getPurchasesByCustomer(@PathVariable Long customerId) {
        List<CustomerProduct> purchases = purchaseService.getPurchasesByCustomer(customerId);
        return ResponseEntity.ok(purchases);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<CustomerProduct>> getPurchasesByProduct(@PathVariable Long productId) {
        List<CustomerProduct> purchases = purchaseService.getPurchasesByProduct(productId);
        return ResponseEntity.ok(purchases);
    }
}
