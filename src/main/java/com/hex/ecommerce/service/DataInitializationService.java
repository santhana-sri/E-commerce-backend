package com.hex.ecommerce.service;

import com.hex.ecommerce.enums.Category;
import com.hex.ecommerce.enums.Role;
import com.hex.ecommerce.model.Customer;
import com.hex.ecommerce.model.Product;
import com.hex.ecommerce.model.User;
import com.hex.ecommerce.model.Vendor;
import com.hex.ecommerce.repository.CustomerRepository;
import com.hex.ecommerce.repository.ProductRepository;
import com.hex.ecommerce.repository.UserRepository;
import com.hex.ecommerce.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataInitializationService implements CommandLineRunner {

    private final VendorRepository vendorRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (vendorRepository.count() == 0) {
            initializeData();
        }
    }

    private void initializeData() {
        // Create Vendors
        Vendor vendor1 = new Vendor("TechCorp", "Mumbai");
        Vendor vendor2 = new Vendor("ElectroWorld", "Delhi");
        Vendor vendor3 = new Vendor("GadgetHub", "Bangalore");
        
        List<Vendor> vendors = vendorRepository.saveAll(List.of(vendor1, vendor2, vendor3));

        // Create Products
        List<Product> products = List.of(
            new Product("iPhone 15 Pro", "Latest Apple smartphone with A17 Pro chip", Category.MOBILE, 99999.0, 50, vendors.get(0)),
            new Product("Samsung Galaxy S24", "Premium Android smartphone", Category.MOBILE, 79999.0, 30, vendors.get(1)),
            new Product("OnePlus 12", "Flagship killer smartphone", Category.MOBILE, 64999.0, 25, vendors.get(2)),
            new Product("MacBook Pro M3", "Professional laptop for developers", Category.LAPTOP, 199999.0, 15, vendors.get(0)),
            new Product("Dell XPS 13", "Ultra-portable business laptop", Category.LAPTOP, 89999.0, 20, vendors.get(1)),
            new Product("HP Spectre x360", "2-in-1 convertible laptop", Category.LAPTOP, 109999.0, 18, vendors.get(2)),
            new Product("iMac 24-inch", "All-in-one desktop computer", Category.DESKTOP, 129999.0, 10, vendors.get(0)),
            new Product("HP Pavilion Desktop", "Budget-friendly desktop PC", Category.DESKTOP, 45999.0, 12, vendors.get(1)),
            new Product("Alienware Aurora R15", "High-performance gaming desktop", Category.DESKTOP, 189999.0, 8, vendors.get(2)),
            new Product("Google Pixel 8", "AI-powered Android phone", Category.MOBILE, 69999.0, 35, vendors.get(0)),
            new Product("Lenovo ThinkPad X1", "Business ultrabook", Category.LAPTOP, 149999.0, 22, vendors.get(1)),
            new Product("Custom Gaming PC", "High-end gaming desktop", Category.DESKTOP, 159999.0, 6, vendors.get(2))
        );
        
        productRepository.saveAll(products);

        // Create Customers
        List<Customer> customers = List.of(
            new Customer("Rahul Sharma", "Mumbai"),
            new Customer("Priya Patel", "Delhi"),
            new Customer("Amit Kumar", "Bangalore"),
            new Customer("Sneha Singh", "Chennai"),
            new Customer("Vikram Reddy", "Hyderabad")
        );
        
        customerRepository.saveAll(customers);

        // Create Sample Users for Authentication
        List<User> users = List.of(
            new User("admin", "admin@ecommerce.com", passwordEncoder.encode("admin123"), "System Administrator", Role.ADMIN),
            new User("customer1", "customer1@email.com", passwordEncoder.encode("customer123"), "John Doe", Role.CUSTOMER),
            new User("customer2", "customer2@email.com", passwordEncoder.encode("customer123"), "Jane Smith", Role.CUSTOMER),
            new User("vendor1", "vendor1@email.com", passwordEncoder.encode("vendor123"), "TechCorp Manager", Role.VENDOR),
            new User("vendor2", "vendor2@email.com", passwordEncoder.encode("vendor123"), "ElectroWorld Manager", Role.VENDOR)
        );
        
        userRepository.saveAll(users);

        System.out.println("Sample data initialized successfully!");
        System.out.println("=== Sample Users Created ===");
        System.out.println("Admin: username=admin, password=admin123");
        System.out.println("Customer1: username=customer1, password=customer123");
        System.out.println("Customer2: username=customer2, password=customer123");
        System.out.println("Vendor1: username=vendor1, password=vendor123");
        System.out.println("Vendor2: username=vendor2, password=vendor123");
    }
}
