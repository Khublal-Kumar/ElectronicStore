package com.red.ElectronicStore.controllers;

import com.red.ElectronicStore.dto.CategoryDTO;
import com.red.ElectronicStore.dto.PageableResponse;
import com.red.ElectronicStore.dto.ProductDTO;
import com.red.ElectronicStore.dto.UserDTO;
import com.red.ElectronicStore.exceptions.BadApiRequest;
import com.red.ElectronicStore.helper.ApiResponseMessage;
import com.red.ElectronicStore.helper.ImageApiResponse;
import com.red.ElectronicStore.services.CategoryService;
import com.red.ElectronicStore.exceptions.CategoryNotFoundException;
import com.red.ElectronicStore.services.FileService;
import com.red.ElectronicStore.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@Validated
public class CategoryController {

    @Value("${category.profile.image.path}")
    private  String imageUploadPath;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    // Create a new category
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    // Update an existing category by ID
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable String categoryId,
            @Valid @RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryDTO updatedCategory = categoryService.updateCategory(categoryId, categoryDTO);
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
        } catch (CategoryNotFoundException ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Delete a category by ID
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId) {
        try {
            categoryService.deleteCategory(categoryId);

            ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                    .message("Category deleted successfully")
                    .status(HttpStatus.ACCEPTED)
                    .success(true)
                    .build();
            return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);
        } catch (CategoryNotFoundException ex) {

            ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                    .message("Category not found!!!")
                    .status(HttpStatus.BAD_REQUEST)
                    .success(true)
                    .build();
            return new ResponseEntity<>(apiResponseMessage, HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Get a category by ID
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable String categoryId) {
        try {
            Optional<CategoryDTO> categoryById = categoryService.getCategoryById(categoryId);
            CategoryDTO categoryDTO = categoryById.get();
            return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
        } catch (CategoryNotFoundException ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Get all categories
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDTO>> getAllCategories(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "categoryTitle",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir
    ) {
        PageableResponse<CategoryDTO> categories = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(categories,HttpStatus.OK);

    }

    // Get a category by title
    @GetMapping("/title/{title}")
    public ResponseEntity<CategoryDTO> getCategoryByTitle(@PathVariable String title) {
        try {
            CategoryDTO categoryDTO = categoryService.getCategoryByTitle(title);
            return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
        } catch (CategoryNotFoundException ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Search categories by a keyword in their title
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<CategoryDTO>> searchCategories(@PathVariable String keyword) {
        List<CategoryDTO> categoryDTOList = categoryService.searchCategories(keyword);
        if (categoryDTOList.isEmpty()) {
            return new ResponseEntity<>(categoryDTOList, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categoryDTOList, HttpStatus.OK);
    }

    @PostMapping("/image/{categoryID}")
    public ResponseEntity<ImageApiResponse> uploadUserImage(
            @RequestParam MultipartFile categoryCoverImage,
            @PathVariable String categoryID
    ) throws IOException, BadApiRequest {

        // Validate file
        if (categoryCoverImage.isEmpty()) {
            throw new BadApiRequest("File is empty. Please upload a valid file.");
        }

        // Upload file and get the file name
        String imageName = fileService.uploadFile(categoryCoverImage, imageUploadPath);

        // Fetch user by ID
        Optional<CategoryDTO> categoryById = categoryService.getCategoryById(categoryID);
        if (!categoryById.isPresent()) {
            throw new BadApiRequest("User not found with ID: " + categoryID);
        }

        // Update user's image name
        CategoryDTO categoryDTO = categoryById.get();
        // Check if user already has an image
        if (categoryDTO.getCategoryCoverImage() != null) {
            String existingImagePath = imageUploadPath + "/" + categoryDTO.getCategoryCoverImage();
            File existingImageFile = new File(existingImagePath);

            // Delete the existing image file
            if (existingImageFile.exists() && existingImageFile.isFile()) {
                if (!existingImageFile.delete()) {
                    throw new IOException("Failed to delete existing image: " + categoryDTO.getCategoryCoverImage());
                }
            }
        }

//        userDTO.setImageName(imageName);
        categoryDTO.setCategoryCoverImage(imageName);
//        userService.updateUser(userId, userDTO);
        categoryService.updateCategory(categoryID,categoryDTO);



        // Build the response
        ImageApiResponse response = ImageApiResponse.builder()
                .imageName(imageName)
                .success(true)
                .status(HttpStatus.CREATED)
                .messgae("Your image uploaded successfully!")
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @GetMapping("/image/{categoryID}")
    public void serveUserImage(@PathVariable String categoryID, HttpServletResponse response) throws IOException {

//        Optional<UserDTO> userById = userService.getUserById(categoryId);
        Optional<CategoryDTO> categoryById = categoryService.getCategoryById(categoryID);

//        UserDTO userDTO = userById.get();
        CategoryDTO categoryDTO = categoryById.get();
//        InputStream resource = fileService.getResource(imageUploadPath, userDTO.getImageName());
        InputStream resource = fileService.getResource(imageUploadPath, categoryDTO.getCategoryCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());


    }


    @PostMapping("/{categoryId}/product")
    public ResponseEntity<ProductDTO> createProductWithCategory(
            @PathVariable String categoryId,
            @RequestBody ProductDTO productDTO
    ){
        ProductDTO productWithCategory = productService.createProductWithCategory(productDTO, categoryId);

        return new ResponseEntity<>(productWithCategory,HttpStatus.CREATED);
    }

    @PostMapping("/{categoryId}/product/{productId}")
    public ResponseEntity<ProductDTO> categoryWithProduct(
            @PathVariable String categoryId,
            @PathVariable String productId
    ){
        ProductDTO productDTO = productService.updateCategoryWithProduct(productId, categoryId);

        return new ResponseEntity<>(productDTO,HttpStatus.CREATED);
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDTO>> getProductOfCategory(
            @PathVariable String categoryId,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir
    ){

        PageableResponse<ProductDTO> response = productService.getAllOfCategory(categoryId,pageNumber,pageSize,sortBy,sortDir);

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
