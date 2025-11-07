package com.hex.ecommerce.dto;

import com.hex.ecommerce.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfoDto {
    private Long id;
    private String title;
    private String description;
    private Category category;
    private Double price;
    private Integer stockQuantity;
    private String vendorName;
    private Long vendorId;
    private Long customerCount; // Number of customers who purchased this product
}
