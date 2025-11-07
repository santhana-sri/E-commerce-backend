package com.hex.ecommerce.service;

import com.hex.ecommerce.dto.VendorDto;
import com.hex.ecommerce.exception.InvalidIdException;
import com.hex.ecommerce.model.Vendor;
import com.hex.ecommerce.repository.VendorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;

    public Vendor getById(Long vendorId) {
        return vendorRepository.findById(vendorId)
                .orElseThrow(() -> new InvalidIdException("Vendor Id Invalid: " + vendorId));
    }

    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    public Vendor saveVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    // Get vendors with product count using Streams
    public List<VendorDto> getVendorsWithProductCount() {
        List<Vendor> vendors = vendorRepository.findAll();
        
        return vendors.stream()
                .map(vendor -> new VendorDto(
                        vendor.getId(),
                        vendor.getName(),
                        vendor.getCity(),
                        vendor.getProducts() != null ? vendor.getProducts().size() : 0
                ))
                .sorted((v1, v2) -> Integer.compare(v2.getProductCount(), v1.getProductCount()))
                .collect(Collectors.toList());
    }

    public List<Vendor> getVendorsByCity(String city) {
        return vendorRepository.findByCity(city);
    }
}
