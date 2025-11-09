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
- **Security**: Spring Security with Basic Authentication
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
- 5 Users for authentication:
  - **Admin**: username=`admin`, password=`admin123`
  - **Customer1**: username=`customer1`, password=`customer123`
  - **Customer2**: username=`customer2`, password=`customer123`
  - **Vendor1**: username=`vendor1`, password=`vendor123`
  - **Vendor2**: username=`vendor2`, password=`vendor123`

## API Documentation

### 🔐 Authentication APIs

#### Signup
```http
POST /api/auth/signup
```
**Body:**
```json
{
    "username": "newuser",
    "email": "newuser@email.com",
    "password": "password123",
    "fullName": "New User",
    "role": "CUSTOMER"
}
```
**Roles**: `CUSTOMER`, `VENDOR`, `ADMIN`

#### Login (Basic Authentication)
```http
GET /api/auth/login
Authorization: Basic <base64(username:password)>
```
**Example with credentials:**
- Username: `customer1`
- Password: `customer123`
- Authorization: `Basic Y3VzdG9tZXIxOmN1c3RvbWVyMTIz`

**Response:**
```json
{
    "id": 2,
    "username": "customer1",
    "email": "customer1@email.com",
    "fullName": "John Doe",
    "role": "CUSTOMER",
    "createdAt": "2025-11-08T23:15:30.123456",
    "enabled": true
}
```

#### Test Endpoints
- `GET /api/auth/public/hello` - Public access
- `GET /api/auth/user/hello` - Authenticated users
- `GET /api/auth/admin/hello` - Admin only
- `GET /api/auth/customer/hello` - Customer only
- `GET /api/auth/vendor/hello` - Vendor only

### 🛍️ Product APIs
- `POST /api/products` - Add new product (**Vendor/Admin only**)
- `GET /api/products/{id}` - Get product by ID (**Public**)
- `GET /api/products` - Get all products (**Public**)
- `GET /api/products/filter` - Filter products by category (**Public**)
- `GET /api/products/sort` - Sort products by price using Streams (**Public**)
- `GET /api/products/info-with-customer-count` - Get product info with customer count (**Public**)
- `GET /api/products/category-stats` - Get category statistics using Streams (**Public**)
- `GET /api/products/vendor/{vendorId}` - Get products by vendor (**Public**)
- `GET /api/products/price-range` - Get products by price range (**Public**)

### 🏢 Vendor APIs
- `POST /api/vendors` - Add new vendor (**Admin only**)
- `GET /api/vendors/{id}` - Get vendor by ID (**Public**)
- `GET /api/vendors` - Get all vendors (**Public**)
- `GET /api/vendors/with-product-count` - Get vendors with product count (**Public**)
- `GET /api/vendors/city/{city}` - Get vendors by city (**Public**)

### 👥 Customer APIs
- `POST /api/customers` - Add new customer (**Admin only**)
- `GET /api/customers/{id}` - Get customer by ID (**Admin only**)
- `GET /api/customers` - Get all customers (**Admin only**)
- `GET /api/customers/city/{city}` - Get customers by city (**Admin only**)

### 🛒 Purchase APIs
- `POST /api/purchases` - Purchase product (**Customer only**)
- `GET /api/purchases/customer/{customerId}` - Get purchases by customer (**Customer/Admin**)
- `GET /api/purchases/product/{productId}` - Get purchases by product (**Vendor/Admin**)

## Java Streams Implementation

The application extensively uses Java Streams for:
1. **Filtering**: Category-based product filtering
2. **Sorting**: Price-based product sorting (ASC/DESC) with manual pagination
3. **Grouping**: Category statistics and vendor product counts
4. **Mapping**: Entity to DTO conversions
5. **Aggregation**: Statistical calculations and customer counting
6. **Distinct Operations**: Counting unique customers per product

## 🔐 Security Features

### Basic Authentication
- **HTTP Basic Auth**: Uses username/password in Authorization header
- **Session-based**: Spring Security manages authentication sessions
- **Secure Headers**: Standard Basic Authentication header handling

### Role-Based Access Control (RBAC)
- **ADMIN**: Full system access, can manage all resources
- **VENDOR**: Can manage their own products, view purchase analytics
- **CUSTOMER**: Can make purchases, view their order history

### Security Configuration
- **Password Encryption**: BCrypt hashing for secure password storage
- **CSRF Protection**: Disabled for REST API (stateless)
- **CORS**: Configurable for cross-origin requests
- **Authentication Entry Point**: Custom JWT authentication handling

### Protected Endpoints
- **Public**: Product browsing, vendor information
- **Authenticated**: User profile, role-specific actions
- **Role-Specific**: Admin management, vendor operations, customer purchases

## Error Handling

Global exception handling for:
- `InvalidIdException`: When entity not found
- `InsufficientStockException`: When stock is insufficient for purchase
- `UsernameAlreadyExistsException`: When username is already taken
- `EmailAlreadyExistsException`: When email is already in use
- Generic exceptions with proper HTTP status codes
