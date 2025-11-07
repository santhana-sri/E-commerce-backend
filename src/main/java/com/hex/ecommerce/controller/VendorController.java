package com.hex.ecommerce.controller;

import com.hex.ecommerce.dto.VendorDto;
import com.hex.ecommerce.model.Vendor;
import com.hex.ecommerce.service.VendorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@AllArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @PostMapping
    public ResponseEntity<Vendor> addVendor(@RequestBody Vendor vendor) {
        Vendor savedVendor = vendorService.saveVendor(vendor);
        return new ResponseEntity<>(savedVendor, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vendor> getVendorById(@PathVariable Long id) {
        Vendor vendor = vendorService.getById(id);
        return ResponseEntity.ok(vendor);
    }

    @GetMapping
    public ResponseEntity<List<Vendor>> getAllVendors() {
        List<Vendor> vendors = vendorService.getAllVendors();
        return ResponseEntity.ok(vendors);
    }

    // Get vendors with product count (using Streams)
    @GetMapping("/with-product-count")
    public ResponseEntity<List<VendorDto>> getVendorsWithProductCount() {
        List<VendorDto> vendors = vendorService.getVendorsWithProductCount();
        return ResponseEntity.ok(vendors);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Vendor>> getVendorsByCity(@PathVariable String city) {
        List<Vendor> vendors = vendorService.getVendorsByCity(city);
        return ResponseEntity.ok(vendors);
    }
}
