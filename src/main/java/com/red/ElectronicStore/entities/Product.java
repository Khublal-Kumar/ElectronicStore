package com.red.ElectronicStore.entities;



import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    private Double price;

    private Integer quantity;

    private String brand;

    private Boolean availability;

    private Double discount;

    private Double rating;

    private Integer warrantyMonths;

    private String sku;

    private String color;

    private Double weight;

    private String dimensions;

    private String manufacturer;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String imageUrl;

    private String videoUrl;

    private Boolean featured;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}