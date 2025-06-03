package com.example.smartbooking.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CategoryCreateDto {
    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name cannot exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Professional ID is required")
    private Long professionalId;
}