package com.red.ElectronicStore.services;

import com.red.ElectronicStore.dto.AddItemToCartRequest;
import com.red.ElectronicStore.dto.CartDTO;
import com.red.ElectronicStore.exceptions.BadApiRequest;

public interface CartService {

    CartDTO addItemToCart(String userId, AddItemToCartRequest request) throws BadApiRequest;

    void removeItemFromCart(String userId,int cartItem) throws BadApiRequest;

    void clearCart(String userId);

    CartDTO getCartByUser(String userId);

}
