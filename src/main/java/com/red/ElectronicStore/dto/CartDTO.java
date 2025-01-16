package com.red.ElectronicStore.dto;

import com.red.ElectronicStore.entities.CartItem;
import com.red.ElectronicStore.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {


    private String cartId;

    private Date createdAt;

    private UserDTO user;

    private List<CartItemDTO> items = new ArrayList<>();


}
