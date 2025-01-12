package com.red.ElectronicStore.repositories;

import com.red.ElectronicStore.dto.CategoryDTO;
import com.red.ElectronicStore.entities.Category;
import com.red.ElectronicStore.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Marking it as a Repository
@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    // Custom methods if required

    // Find a category by title
    Category findByCategoryTitle(String title);

    // Find all categories containing a keyword in their title (for search functionality)
    List<Category> findByCategoryTitleContaining(String keyword);

    // Count categories with a specific description
    long countByCategoryDescription(String description);

//    Optional<Category> findByCategoryID(String categoryId);

//    Page<Product> findByCategory(Category category);
}
