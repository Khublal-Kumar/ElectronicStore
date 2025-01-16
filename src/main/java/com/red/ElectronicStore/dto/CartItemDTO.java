package com.red.ElectronicStore.dto;

import com.red.ElectronicStore.entities.Cart;
import com.red.ElectronicStore.entities.Product;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {


    private int cartItemId;

    private int quantity;

    private int totalPrice;

    private ProductDTO product;


}
