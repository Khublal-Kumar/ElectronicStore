package com.red.ElectronicStore.controllers;

import com.red.ElectronicStore.dto.CategoryDTO;
import com.red.ElectronicStore.dto.PageableResponse;
import com.red.ElectronicStore.dto.ProductDTO;
import com.red.ElectronicStore.exceptions.BadApiRequest;
import com.red.ElectronicStore.exceptions.CategoryNotFoundException;
import com.red.ElectronicStore.helper.ApiResponseMessage;
import com.red.ElectronicStore.helper.ImageApiResponse;
import com.red.ElectronicStore.services.FileService;
import com.red.ElectronicStore.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {


    private final ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.profile.image.path}")
    private String productImagePath;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Create a new product
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    // Update an existing product
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable String productId, @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(productId, productDTO);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    // Delete a product by ID
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId) {
        try {
            productService.deleteProduct(productId);

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
        }
    }

    // Get product by ID
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable String productId) {
        ProductDTO productDTO = productService.getProductById(productId);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    // Get all products with pagination and sorting
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDTO>> getAllProducts(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir) {

        PageableResponse<ProductDTO> productDTOPageableResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productDTOPageableResponse, HttpStatus.OK);
    }

    // Get products by name with pagination and sorting
    @GetMapping("/search")
    public ResponseEntity<PageableResponse<ProductDTO>> getProductsByName(
            @RequestParam String name,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir) {

        PageableResponse<ProductDTO> productDTOPageableResponse = productService.getProductsByName(name, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productDTOPageableResponse, HttpStatus.OK);
    }

    // Get products by brand with pagination and sorting
    @GetMapping("/brand")
    public ResponseEntity<PageableResponse<ProductDTO>> getProductsByBrand(
            @RequestParam String brand,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir) {

        PageableResponse<ProductDTO> productDTOPageableResponse = productService.getProductsByBrand(brand, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productDTOPageableResponse, HttpStatus.OK);
    }

//    // Get products by category with pagination and sorting
//    @GetMapping("/category")
//    public ResponseEntity<PageableResponse<ProductDTO>> getProductsByCategory(
//            @RequestParam String category,
//            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
//            @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
//            @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
//            @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir) {
//
//        PageableResponse<ProductDTO> productDTOPageableResponse = productService.getProductsByCategory(category, pageNumber, pageSize, sortBy, sortDir);
//        return new ResponseEntity<>(productDTOPageableResponse, HttpStatus.OK);
//    }

    // Get products by price range with pagination and sorting
    @GetMapping("/price-range")
    public ResponseEntity<PageableResponse<ProductDTO>> getProductsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir) {

        PageableResponse<ProductDTO> productDTOPageableResponse = productService.getProductsByPriceRange(minPrice, maxPrice, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productDTOPageableResponse, HttpStatus.OK);
    }

    // Get products by rating with pagination and sorting
    @GetMapping("/rating")
    public ResponseEntity<PageableResponse<ProductDTO>> getProductsByRating(
            @RequestParam Double minRating,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir) {

        PageableResponse<ProductDTO> productDTOPageableResponse = productService.getProductsByRating(minRating, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productDTOPageableResponse, HttpStatus.OK);
    }

    // Get products by discount with pagination and sorting
    @GetMapping("/discount")
    public ResponseEntity<PageableResponse<ProductDTO>> getProductsByDiscount(
            @RequestParam Double minDiscount,
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir) {

        PageableResponse<ProductDTO> productDTOPageableResponse = productService.getProductsByDiscount(minDiscount, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productDTOPageableResponse, HttpStatus.OK);
    }

    // Get featured products with pagination and sorting
    @GetMapping("/featured")
    public ResponseEntity<PageableResponse<ProductDTO>> getFeaturedProducts(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir) {

        PageableResponse<ProductDTO> productDTOPageableResponse = productService.getFeaturedProducts(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productDTOPageableResponse, HttpStatus.OK);
    }

    // Get available products with pagination and sorting
    @GetMapping("/available")
    public ResponseEntity<PageableResponse<ProductDTO>> getAvailableProducts(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "5",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "desc",required = false) String sortDir) {

        PageableResponse<ProductDTO> productDTOPageableResponse = productService.getAvailableProducts(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(productDTOPageableResponse, HttpStatus.OK);
    }

    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageApiResponse> uploadProductImage(
            @PathVariable String productId,
            @RequestParam MultipartFile productImage
            ) throws IOException, BadApiRequest {
        String uploadedFile = fileService.uploadFile(productImage, productImagePath);

        ProductDTO productDTO = productService.getProductById(productId);
        productDTO.setImageUrl(uploadedFile);
        ProductDTO updatedProduct = productService.updateProduct(productId, productDTO);
        ImageApiResponse imageApiResponse = ImageApiResponse.builder().imageName(updatedProduct.getImageUrl())
                .messgae("Uploaded successfully in" + productId)
                .status(HttpStatus.CREATED)
                .success(true)
                .build();
        return new ResponseEntity<>(imageApiResponse,HttpStatus.OK);

    }

    @GetMapping("/image/{productId}")
    public void serveUserImage(@PathVariable String productId, HttpServletResponse response) throws IOException {

        ProductDTO productDTO = productService.getProductById(productId);


        InputStream resource = fileService.getResource(productImagePath, productDTO.getImageUrl());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());


    }

}
