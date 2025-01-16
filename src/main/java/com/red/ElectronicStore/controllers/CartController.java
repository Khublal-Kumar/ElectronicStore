package com.red.ElectronicStore.controllers;

import com.red.ElectronicStore.dto.AddItemToCartRequest;
import com.red.ElectronicStore.dto.CartDTO;
import com.red.ElectronicStore.exceptions.UserNotFoundException;
import com.red.ElectronicStore.helper.ApiResponseMessage;
import com.red.ElectronicStore.repositories.UserRepository;
import com.red.ElectronicStore.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    //add item to cart
    @PostMapping("/{userId}")
    public ResponseEntity<CartDTO> addItemsToCart(
            @PathVariable String userId,
            @RequestBody AddItemToCartRequest addItemToCartRequest){

        CartDTO cartDTO = cartService.addItemToCart(userId, addItemToCartRequest);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(
            @PathVariable String userId,
            @PathVariable int itemId
    ){
        userRepository.findById(userId)
                .orElseThrow((() -> new UserNotFoundException("User not found!!!")));


        cartService.removeItemFromCart(userId,itemId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("Items are removed form cart!!!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message,HttpStatus.CREATED);

    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId){

        cartService.clearCart(userId);

        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("Your cart is clear !!!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message,HttpStatus.CREATED);
    }

    @GetMapping("{userId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable String userId){

        CartDTO cartByUser = cartService.getCartByUser(userId);


        return new ResponseEntity<>(cartByUser,HttpStatus.OK);
    }



}

