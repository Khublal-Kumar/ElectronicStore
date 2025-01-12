package com.red.ElectronicStore.services.Impl;

import com.red.ElectronicStore.dto.PageableResponse;
import com.red.ElectronicStore.dto.ProductDTO;
import com.red.ElectronicStore.entities.Category;
import com.red.ElectronicStore.entities.Product;
import com.red.ElectronicStore.exceptions.CategoryNotFoundException;
import com.red.ElectronicStore.exceptions.ProductNotFoundException;
import com.red.ElectronicStore.helper.PageResponseHandler;
import com.red.ElectronicStore.repositories.CategoryRepository;
import com.red.ElectronicStore.repositories.ProductRepository;
import com.red.ElectronicStore.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class ProductServiceImpl implements ProductService {


    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper mapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper,CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.mapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        // Convert DTO to Entity
        Product product = mapper.map(productDTO, Product.class);

        String productId = UUID.randomUUID().toString();

        product.setId(productId);

        // Set createdAt and updatedAt fields
        product.setCreatedAt(java.time.LocalDateTime.now());
        product.setUpdatedAt(java.time.LocalDateTime.now());

        // Save the product entity to the database
        Product savedProduct = productRepository.save(product);

        // Convert the saved product entity back to DTO and return
        return mapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProduct(String productId, ProductDTO productDTO) {
        // Find the product by ID
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found"));

        // Update product fields
//        mapper.map(productDTO, existingProduct);
        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setQuantity(productDTO.getQuantity());
        existingProduct.setBrand(productDTO.getBrand());
//        existingProduct.setCategory(productDTO.getCategory());
        existingProduct.setAvailability(productDTO.getAvailability());
        existingProduct.setDiscount(productDTO.getDiscount());
        existingProduct.setRating(productDTO.getRating());
        existingProduct.setWarrantyMonths(productDTO.getWarrantyMonths());
        existingProduct.setSku(productDTO.getSku());
        existingProduct.setColor(productDTO.getColor());
        existingProduct.setWeight(productDTO.getWeight());
        existingProduct.setDimensions(productDTO.getDimensions());
        existingProduct.setManufacturer(productDTO.getManufacturer());
        existingProduct.setImageUrl(productDTO.getImageUrl());
        existingProduct.setVideoUrl(productDTO.getVideoUrl());
        existingProduct.setFeatured(productDTO.getFeatured());
        existingProduct.setUpdatedAt(java.time.LocalDateTime.now());



        // Save the updated product
        Product updatedProduct = productRepository.save(existingProduct);

        // Convert to DTO and return
        return mapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public void deleteProduct(String productId) {
        // Find the product by ID and check if it exists
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found"));

        // Delete the product
        productRepository.delete(existingProduct);
    }

    @Override
    public ProductDTO getProductById(String productId) {
        // Find the product by ID
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found"));

        // Convert the product entity to DTO and return
        return mapper.map(product, ProductDTO.class);
    }

    @Override
    public PageableResponse<ProductDTO> getAllProducts(int pageNumber, int pageSize, String sortBy,String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        // Fetch all products from the repository
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);

        Page<Product> pages = productRepository.findAll(pageable);

        PageableResponse<ProductDTO> productDTOPageableResponse = PageResponseHandler.getPageableResponse(pages, ProductDTO.class);

        // Convert all products to DTOs and return
        return productDTOPageableResponse;
    }

    @Override
    public PageableResponse<ProductDTO> getProductsByName(String name,int pageNumber, int pageSize, String sortBy,String sortDir) {


        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Product> pages = productRepository.findByNameContainingIgnoreCase(name, pageable);

        PageableResponse<ProductDTO> productDTOPageableResponse = PageResponseHandler.getPageableResponse(pages, ProductDTO.class);

        return productDTOPageableResponse;
    }

    @Override
    public PageableResponse<ProductDTO> getProductsByBrand(String brand,int pageNumber, int pageSize, String sortBy,String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Product> pages = productRepository.findByBrandIgnoreCase(brand, pageable);

        PageableResponse<ProductDTO> productDTOPageableResponse = PageResponseHandler.getPageableResponse(pages, ProductDTO.class);

        return productDTOPageableResponse;

    }

//    @Override
//    public PageableResponse<ProductDTO> getProductsByCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
//
//
////        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
////        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
////        Page<Product> pages = productRepository.findByCategoryIgnoreCase(categoryId, pageable);
////        PageableResponse<ProductDTO> productDTOPageableResponse = PageResponseHandler.getPageableResponse(pages, ProductDTO.class);
////        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
////        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
//        Category category = categoryRepository.findById(categoryId)
//                .orElseThrow((() -> new CategoryNotFoundException("category not found!!! " + categoryId)));
//        Page<Product> productPage = productRepository.findByCategory(category);
//
//        return PageResponseHandler.getPageableResponse(productPage,ProductDTO.class);
//
//
//    }



    @Override
    public PageableResponse<ProductDTO> getProductsByPriceRange(Double minPrice, Double maxPrice,int pageNumber, int pageSize, String sortBy,String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Product> pages = productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        PageableResponse<ProductDTO> productDTOPageableResponse = PageResponseHandler.getPageableResponse(pages, ProductDTO.class);

        return productDTOPageableResponse;
    }

    @Override
    public PageableResponse<ProductDTO> getProductsByRating(Double minRating,int pageNumber, int pageSize, String sortBy,String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Product> pages = productRepository.findByRatingGreaterThanEqual(minRating, pageable);

        PageableResponse<ProductDTO> productDTOPageableResponse = PageResponseHandler.getPageableResponse(pages, ProductDTO.class);

        return productDTOPageableResponse;
    }

    @Override
    public PageableResponse<ProductDTO> getProductsByDiscount(Double minDiscount,int pageNumber, int pageSize, String sortBy,String sortDir) {


        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Product> pages = productRepository.findByDiscountGreaterThanEqual(minDiscount, pageable);
        PageableResponse<ProductDTO> productDTOPageableResponse = PageResponseHandler.getPageableResponse(pages, ProductDTO.class);

        return productDTOPageableResponse;
    }

    @Override
    public PageableResponse<ProductDTO> getFeaturedProducts(int pageNumber, int pageSize, String sortBy,String sortDir) {


        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Product> pages = productRepository.findByFeatured(true, pageable);
        PageableResponse<ProductDTO> productDTOPageableResponse = PageResponseHandler.getPageableResponse(pages, ProductDTO.class);

        return productDTOPageableResponse;
    }

    @Override
    public PageableResponse<ProductDTO> getAvailableProducts(int pageNumber, int pageSize, String sortBy,String sortDir) {


        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);

        Page<Product> pages = productRepository.findByAvailability(true, pageable);
        PageableResponse<ProductDTO> productDTOPageableResponse = PageResponseHandler.getPageableResponse(pages, ProductDTO.class);

        return productDTOPageableResponse;
    }

    @Override
    public ProductDTO createProductWithCategory(ProductDTO productDTO, String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow((() -> new CategoryNotFoundException("Category Not Found !!!! " + categoryId)));
        Product product = mapper.map(productDTO, Product.class);

        String productId = UUID.randomUUID().toString();

        product.setId(productId);

        // Set createdAt and updatedAt fields
        product.setCreatedAt(java.time.LocalDateTime.now());
        product.setUpdatedAt(java.time.LocalDateTime.now());
        product.setCategory(category);

        // Save the product entity to the database
        Product savedProduct = productRepository.save(product);

        // Convert the saved product entity back to DTO and return
        return mapper.map(savedProduct, ProductDTO.class);

    }

    @Override
    public ProductDTO updateCategoryWithProduct(String productId, String categoryId) {


        Product product = productRepository.findById(productId)
                .orElseThrow((() -> new ProductNotFoundException("Product is not Found !!! " + productId)));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow((() -> new CategoryNotFoundException("category not found!!! " + categoryId)));

        product.setCategory(category);

        Product save = productRepository.save(product);

        return mapper.map(save,ProductDTO.class);
    }

    @Override
    public PageableResponse<ProductDTO> getAllOfCategory(String categoryId,int pageNumber, int pageSize, String sortBy,String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow((() -> new CategoryNotFoundException("category not found!!! " + categoryId)));
        Page<Product> page = productRepository.findByCategory(category,pageable);

        return PageResponseHandler.getPageableResponse(page,ProductDTO.class);
    }


}
