# E-Commerce Application

An enterprise-level e-commerce application built with Spring Boot, JPA, and Java Streams.

## Features

### Core Entities
- **Product**: Contains id, title, description, category, price, stock quantity, and vendor relationship
- **Vendor**: Contains id, name, city, and one-to-many relationship with products
- **Customer**: Contains id, name, city, and many-to-many relationship with products through purchases
- **CustomerProduct**: Junction table for customer-product relationship with purchase details

### Key APIs

#### 1. Product Filter API
```
GET /api/products/filter?category=MOBILE
```
- Filters products by category (MOBILE, LAPTOP, DESKTOP)
- Category is mandatory parameter
- No pagination (as per requirement)

#### 2. Product Sorting API (using Java Streams with Pagination)
```
GET /api/products/sort?direction=ASC&page=0&size=10
GET /api/products/sort?direction=DESC&page=0&size=10
```
- Sorts products by price in ascending or descending order
- Uses Java Streams for sorting implementation
- Supports pagination

#### 3. Category Statistics API (using Java Streams)
```
GET /api/products/category-stats
```
- Displays number of products in each category
- Uses Java Streams for grouping and counting
- Results sorted by product count in descending order

#### 4. Customer Purchase API
```
POST /api/purchases
{
    "customerId": 1,
    "productId": 2,
    "quantity": 1
}
```
- Allows customers to purchase products
- Updates stock quantity automatically
- Validates stock availability

#### 5. Product Info with Customer Count API
```
GET /api/products/info-with-customer-count
```
- Displays product information with number of customers who purchased each product
- Uses Java Streams for counting unique customers per product

#### 6. Vendor Management
```
GET /api/vendors/with-product-count
```
- Displays vendors with their product counts
- Uses Java Streams for aggregation

## Database Schema

### Tables
- `vendors` (id, name, city)
- `products` (id, title, description, category, price, stock_quantity, vendor_id)
- `customers` (id, name, city)
- `customer_product` (id, customer_id, product_id, date_of_purchase, quantity, total_cost)

### Relationships
- Vendor → Product: One-to-Many
- Customer → CustomerProduct: One-to-Many
- Product → CustomerProduct: One-to-Many
- Customer ↔ Product: Many-to-Many (through CustomerProduct)

## Technology Stack

- **Framework**: Spring Boot 3.5.7
- **Database**: MySQL
- **ORM**: JPA/Hibernate
- **Build Tool**: Maven
- **Java Version**: 21
- **Additional**: Lombok for boilerplate code reduction

## Getting Started

### Prerequisites
- Java 21
- MySQL Server
- Maven

### Setup
1. Clone the repository
2. Update database credentials in `application.properties`
3. Run the application: `mvn spring-boot:run`
4. The application will start on port 8082

### Sample Data
The application automatically initializes with sample data including:
- 3 Vendors (TechCorp, ElectroWorld, GadgetHub)
- 12 Products across all categories
- 5 Customers

## API Documentation

### Product APIs
- `POST /api/products` - Add new product
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products` - Get all products
- `GET /api/products/filter` - Filter products by category (no pagination)
- `GET /api/products/sort` - Sort products by price using Streams (with pagination)
- `GET /api/products/info-with-customer-count` - Get product info with customer count
- `GET /api/products/category-stats` - Get category statistics using Streams
- `GET /api/products/vendor/{vendorId}` - Get products by vendor
- `GET /api/products/price-range` - Get products by price range

### Vendor APIs
- `POST /api/vendors` - Add new vendor
- `GET /api/vendors/{id}` - Get vendor by ID
- `GET /api/vendors` - Get all vendors
- `GET /api/vendors/with-product-count` - Get vendors with product count
- `GET /api/vendors/city/{city}` - Get vendors by city

### Customer APIs
- `POST /api/customers` - Add new customer
- `GET /api/customers/{id}` - Get customer by ID
- `GET /api/customers` - Get all customers
- `GET /api/customers/city/{city}` - Get customers by city

### Purchase APIs
- `POST /api/purchases` - Purchase product
- `GET /api/purchases/customer/{customerId}` - Get purchases by customer
- `GET /api/purchases/product/{productId}` - Get purchases by product

## Java Streams Implementation

The application extensively uses Java Streams for:
1. **Filtering**: Category-based product filtering
2. **Sorting**: Price-based product sorting (ASC/DESC) with manual pagination
3. **Grouping**: Category statistics and vendor product counts
4. **Mapping**: Entity to DTO conversions
5. **Aggregation**: Statistical calculations and customer counting
6. **Distinct Operations**: Counting unique customers per product

## Error Handling

Global exception handling for:
- `InvalidIdException`: When entity not found
- `InsufficientStockException`: When stock is insufficient for purchase
- Generic exceptions with proper HTTP status codes
