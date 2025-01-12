package com.red.ElectronicStore.services.Impl;


import com.red.ElectronicStore.dto.CategoryDTO;
import com.red.ElectronicStore.dto.PageableResponse;
import com.red.ElectronicStore.entities.Category;
import com.red.ElectronicStore.exceptions.CategoryNotFoundException;
import com.red.ElectronicStore.exceptions.UserNotFoundException;
import com.red.ElectronicStore.helper.PageResponseHandler;
import com.red.ElectronicStore.repositories.CategoryRepository;
import com.red.ElectronicStore.services.CategoryService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    @Autowired
    private ModelMapper mapper;

    @Value("${category.profile.image.path}")
    private  String imageUploadPath;

    // Create a new category
    @Override
    public CategoryDTO createCategory(CategoryDTO  categoryDTO) {

        String categoryId = UUID.randomUUID().toString();
        categoryDTO.setCategoryID(categoryId);
        // Convert DTO to entity
        Category category = convertToEntity(categoryDTO);



        // Save the category entity to the database
        Category savedCategory = categoryRepository.save(category);

        // Convert the saved entity back to DTO and return
        return convertToDto(savedCategory);
    }

    // Update an existing category by ID
    @Override
    public CategoryDTO updateCategory(String categoryId, CategoryDTO categoryDTO) {
        // Find the category by ID
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with ID " + categoryId + " not found"));
//        Optional<CategoryDTO> byCategoryId = categoryRepository.findByCategoryId(categoryId);


        // Update the existing category with the new details from the DTO
        existingCategory.setCategoryTitle(categoryDTO.getCategoryTitle());
        existingCategory.setCategoryDescription(categoryDTO.getCategoryDescription());
        existingCategory.setCategoryCoverImage(categoryDTO.getCategoryCoverImage());

        // Save the updated category to the database
        Category updatedCategory = categoryRepository.save(existingCategory);

        // Convert the updated entity back to DTO and return
        return convertToDto(updatedCategory);
    }

    // Delete a category by ID
    @Override
    public void deleteCategory(String categoryId) throws IOException {
        // Check if the category exists before attempting to delete
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with ID " + categoryId + " not found"));

        // Get the category cover image
        String categoryCoverImage = category.getCategoryCoverImage();

        if (categoryCoverImage != null && !categoryCoverImage.isEmpty()) {
            // Construct the full path of the cover image
            String fullPathOfCoverImage = imageUploadPath + File.separator + categoryCoverImage;

            Path path = Paths.get(fullPathOfCoverImage);

            // Check if the file exists before deleting
            if (Files.exists(path)) {
                Files.delete(path); // Delete the file if it exists
            }
        }

        // Delete the category
        categoryRepository.delete(category);
    }


    // Get a category by ID
    @Override
    public Optional<CategoryDTO> getCategoryById(String categoryId) {
        // Find the category by ID
        Optional<Category> byCategoryId = categoryRepository.findById(categoryId);

        if (byCategoryId.isPresent()){
            return Optional.of(convertToDto(byCategoryId.get()));
        }else {
            throw new UserNotFoundException("User with ID " + categoryId + " not found.");
        }

    }

    // Get all categories
    @Override
    public PageableResponse<CategoryDTO> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        // Fetch all categories from the database
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Category> page=  categoryRepository.findAll(pageable);

        PageableResponse<CategoryDTO> categoryDTOPageableResponse = PageResponseHandler.getPageableResponse(page, CategoryDTO.class);

        // Convert the list of category entities to a list of DTOs
        return categoryDTOPageableResponse;

    }

//    public PageableResponse<UserDTO> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {
//        // Get all users and convert them to DTOs
//
//        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
//
//        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
//
//        Page<User> page=  userRepository.findAll(pageable);
//
//        PageableResponse<UserDTO> userDTOPageableResponse = PageResponseHandler.getPageableResponse(page, UserDTO.class);
//
//
//        return userDTOPageableResponse;
//    }

    // Get a category by title
    @Override
    public CategoryDTO getCategoryByTitle(String title) {
        // Find the category by its title
        Category category = categoryRepository.findByCategoryTitle(title);

        if (category == null) {
            throw new CategoryNotFoundException("Category with title " + title + " not found");
        }

        // Convert the category entity to DTO and return
        return convertToDto(category);
    }

    // Search categories by a keyword in their title
    @Override
    public List<CategoryDTO> searchCategories(String keyword) {
        // Find categories by keyword in their title
        List<Category> categories = categoryRepository.findByCategoryTitleContaining(keyword);

        // If no categories found, return an empty list
        if (categories.isEmpty()) {
            return List.of();
        }

        // Convert the list of category entities to a list of DTOs
        return categories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }



    public CategoryDTO convertToDto(Category category) {
        return mapper.map(category,CategoryDTO.class);
    }

    public Category convertToEntity(CategoryDTO categoryDTO) {
        return mapper.map(categoryDTO, Category.class);
    }
}

