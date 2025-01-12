package com.red.ElectronicStore.services;

import com.red.ElectronicStore.dto.CategoryDTO;
import com.red.ElectronicStore.dto.PageableResponse;
import com.red.ElectronicStore.entities.Category;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CategoryService {

    // Create a new category
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    // Update an existing category by ID
    CategoryDTO updateCategory(String categoryId, CategoryDTO categoryDTO);

    // Delete a category by ID
    void deleteCategory(String categoryId) throws IOException;

    // Get a category by ID
    Optional<CategoryDTO> getCategoryById(String categoryId);

    // Get all categories
    PageableResponse<CategoryDTO> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir);

    // Get a category by title
    CategoryDTO getCategoryByTitle(String title);

    // Search categories by a keyword in their title
    List<CategoryDTO> searchCategories(String keyword);
}
