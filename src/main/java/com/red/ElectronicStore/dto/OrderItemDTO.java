package com.red.ElectronicStore.dto;

import com.red.ElectronicStore.entities.Order;
import com.red.ElectronicStore.entities.Product;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {

    private int orderItemId;

    private int quantity;

    private Double totalPrice;

    private ProductDTO product;
    

}

