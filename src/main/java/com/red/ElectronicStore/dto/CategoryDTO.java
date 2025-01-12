package com.red.ElectronicStore.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.red.ElectronicStore.entities.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {


    private String categoryID;

    @NotBlank(message = "Category Title must be required!!!")
    @Size(min = 2,max = 25,message = "categoryTitle must be in required range 2-25")
    private String categoryTitle;

    @NotBlank(message = "Category Description must be required!!!")
    @Size(min = 2,max = 100,message = "categoryDescription must be in required range 2-100")
    private String categoryDescription;

    private String categoryCoverImage;

//    @JsonIgnore
//    private List<ProductDTO> productDTO;
}
