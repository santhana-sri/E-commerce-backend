package com.hex.ecommerce.dto;

import com.hex.ecommerce.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryStatsDto {
    private Category category;
    private Long productCount;
}
