package com.tusuapp.coreapi.models.dtos.dropdowns;

import com.tusuapp.coreapi.models.Category;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryDto {

    private Long id;
    private String name;
    private String type;
    private Integer position;
    private String textColor;
    private String backgroundColor;
    private LocalDateTime publishedAt;
    private Integer createdBy;
    private Integer updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CategoryDto fromCategory(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setType(category.getType());
        dto.setPosition(category.getPosition());
        dto.setTextColor(category.getTextColor());
        dto.setBackgroundColor(category.getBackgroundColor());
        dto.setPublishedAt(category.getPublishedAt());
        dto.setCreatedBy(category.getCreatedBy());
        dto.setUpdatedBy(category.getUpdatedBy());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }
}