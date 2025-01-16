package com.red.ElectronicStore.dto;

import com.red.ElectronicStore.entities.Category;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {


    private String id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name can be at most 100 characters long")
    private String name;

    @Size(max = 500, message = "Description can be at most 500 characters long")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @NotBlank(message = "Brand is required")
    private String brand;



    @NotNull(message = "Availability is required")
    private Boolean availability;

    @DecimalMin(value = "0.0", inclusive = true, message = "Discount cannot be negative")
    @DecimalMax(value = "100.0", inclusive = true, message = "Discount cannot exceed 100%")
    private Double discount;

    @DecimalMin(value = "0.0", inclusive = true, message = "Rating cannot be negative")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating cannot exceed 5.0")
    private Double rating;

    @NotNull(message = "Warranty is required")
    @Min(value = 0, message = "Warranty cannot be negative")
    private Integer warrantyMonths;

    @NotBlank(message = "SKU is required")
    private String sku;

    @NotBlank(message = "Color is required")
    private String color;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be positive")
    private Double weight;

    @NotBlank(message = "Dimensions are required")
    private String dimensions;

    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;

    @NotBlank(message = "Image URL is required")
    @Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be valid")
    private String imageUrl;

    @Pattern(regexp = "^(http|https)://.*$", message = "Video URL must be valid")
    private String videoUrl;

    @NotNull(message = "Featured status is required")
    private Boolean featured;


    private CategoryDTO category;

}
