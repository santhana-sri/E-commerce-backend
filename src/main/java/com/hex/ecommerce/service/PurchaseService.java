package com.hex.ecommerce.service;

import com.hex.ecommerce.dto.PurchaseRequestDto;
import com.hex.ecommerce.exception.InsufficientStockException;
import com.hex.ecommerce.exception.InvalidIdException;
import com.hex.ecommerce.model.Customer;
import com.hex.ecommerce.model.CustomerProduct;
import com.hex.ecommerce.model.Product;
import com.hex.ecommerce.repository.CustomerProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class PurchaseService {

    private final CustomerProductRepository customerProductRepository;
    private final CustomerService customerService;
    private final ProductService productService;

    @Transactional
    public CustomerProduct purchaseProduct(PurchaseRequestDto purchaseRequest) {
        Customer customer = customerService.getById(purchaseRequest.getCustomerId());
        Product product = productService.getById(purchaseRequest.getProductId());

        // Check stock availability
        if (product.getStockQuantity() < purchaseRequest.getQuantity()) {
            throw new InsufficientStockException("Insufficient stock. Available: " + product.getStockQuantity());
        }

        // Calculate total cost
        Double totalCost = product.getPrice() * purchaseRequest.getQuantity();

        // Create purchase record
        CustomerProduct customerProduct = new CustomerProduct(
                customer, product, purchaseRequest.getQuantity(), totalCost
        );

        // Update stock quantity
        product.setStockQuantity(product.getStockQuantity() - purchaseRequest.getQuantity());
        productService.saveProduct(product);

        return customerProductRepository.save(customerProduct);
    }

    public List<CustomerProduct> getPurchasesByCustomer(Long customerId) {
        return customerProductRepository.findByCustomerId(customerId);
    }

    public List<CustomerProduct> getPurchasesByProduct(Long productId) {
        return customerProductRepository.findByProductId(productId);
    }
}
