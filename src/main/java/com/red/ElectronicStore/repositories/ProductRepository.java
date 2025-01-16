package com.red.ElectronicStore.repositories;

import com.red.ElectronicStore.entities.Category;
import com.red.ElectronicStore.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable );

    Page<Product> findByBrandIgnoreCase(String brand,Pageable pageable );

    Page<Product> findByCategoryIgnoreCase(String category,Pageable pageable );

    Page<Product> findByAvailability(Boolean availability,Pageable pageable );

    Page<Product> findByPriceBetween(Double minPrice, Double maxPrice,Pageable pageable );

    Page<Product> findByRatingGreaterThanEqual(Double rating,Pageable pageable );

    Page<Product> findByDiscountGreaterThanEqual(Double discount,Pageable pageable );

    Page<Product> findByFeatured(Boolean featured,Pageable pageable );

    Page<Product> findByCategory(Category category,Pageable pageable);



//    Page<Product> findByCategory(Category category);
}
