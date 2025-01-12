package com.red.ElectronicStore.services;

import com.red.ElectronicStore.dto.PageableResponse;
import com.red.ElectronicStore.dto.ProductDTO;
import com.red.ElectronicStore.entities.Product;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO updateProduct(String productId, ProductDTO productDTO);

    void deleteProduct(String productId);

    ProductDTO getProductById(String productId);

    PageableResponse<ProductDTO> getAllProducts(int pageNumber, int pageSize, String sortBy,String sortDir);

    PageableResponse<ProductDTO> getProductsByName(String name,int pageNumber, int pageSize, String sortBy,String sortDir);

    PageableResponse<ProductDTO>getProductsByBrand(String brand,int pageNumber, int pageSize, String sortBy,String sortDir);

//    PageableResponse<ProductDTO> getProductsByCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);

    PageableResponse<ProductDTO> getProductsByPriceRange(Double minPrice, Double maxPrice,int pageNumber, int pageSize, String sortBy,String sortDir);

    PageableResponse<ProductDTO> getProductsByRating(Double minRating,int pageNumber, int pageSize, String sortBy,String sortDir);

    PageableResponse<ProductDTO> getProductsByDiscount(Double minDiscount,int pageNumber, int pageSize, String sortBy,String sortDir);

    PageableResponse<ProductDTO> getFeaturedProducts(int pageNumber, int pageSize, String sortBy,String sortDir);

    PageableResponse<ProductDTO> getAvailableProducts(int pageNumber, int pageSize, String sortBy,String sortDir);


    //Create product with category
    ProductDTO createProductWithCategory(ProductDTO productDTO,String categoryId);

    //Update category with product
    ProductDTO updateCategoryWithProduct(String productId,String categoryId);

    PageableResponse<ProductDTO> getAllOfCategory(String categoryId,int pageNumber, int pageSize, String sortBy,String sortDir);

}
