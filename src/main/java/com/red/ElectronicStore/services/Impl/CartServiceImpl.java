package com.red.ElectronicStore.services.Impl;

import com.red.ElectronicStore.dto.AddItemToCartRequest;
import com.red.ElectronicStore.dto.CartDTO;
import com.red.ElectronicStore.entities.Cart;
import com.red.ElectronicStore.entities.CartItem;
import com.red.ElectronicStore.entities.Product;
import com.red.ElectronicStore.entities.User;
import com.red.ElectronicStore.exceptions.BadApiRequest;
import com.red.ElectronicStore.exceptions.ProductNotFoundException;
import com.red.ElectronicStore.exceptions.UserNotFoundException;
import com.red.ElectronicStore.repositories.CartItemRepository;
import com.red.ElectronicStore.repositories.CartRepository;
import com.red.ElectronicStore.repositories.ProductRepository;
import com.red.ElectronicStore.repositories.UserRepository;
import com.red.ElectronicStore.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {



    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper mapper;

//    @Override
//    public CartDTO addItemToCart(String userId, AddItemToCartRequest request) {
//
//
//        int quantity = request.getQuantity();
//        String productId = request.getProductId();
//
//        if (quantity<=0){
//            throw new BadApiRequest("Requested quantity is not valid!!!");
//        }
//
//        Product product = productRepository.findById(productId)
//                .orElseThrow((() -> new ProductNotFoundException("Product not found with " + productId)));
//
//        User user = userRepository.findById(userId)
//                .orElseThrow((() -> new UserNotFoundException("User not Found " + userId)));
//
//        Cart cart = null;
//
//        try{
//            cart = cartRepository.findByUser(user).get();
//        } catch (NoSuchElementException e) {
//            cart = new Cart();
//            cart.setCartId(UUID.randomUUID().toString());
//            cart.setCreatedAt(new Date());
//
//        }
//
//
//        AtomicBoolean updated = new AtomicBoolean(false);
//        List<CartItem> items = cart.getItems();
//
//        List<CartItem> updatedItems = items.stream().map(item -> {
//
//            if (item.getProduct().getId().equals(productId)) {
//                //update
//                item.setQuantity(quantity);
//                item.setTotalPrice(quantity * product.getQuantity());
//                updated.set(true);
//            }
//            return item;
//        }).collect(Collectors.toList());
//
//        cart.setItems(updatedItems);
//
//        if (!updated.get()) {
//            CartItem cartItem = new CartItem();
//            cartItem.setQuantity(quantity);
//
//            cartItem.setTotalPrice(quantity * product.getQuantity());
//            cartItem.setProduct(product);
//            cartItem.setCart(cart);
//
//            cart.getItems().add(cartItem);
//        }
//
//        cart.setUser(user);
//        Cart save = cartRepository.save(cart);
//
//
//
//        return mapper.map(save,CartDTO.class);
//    }


    @Override
    public CartDTO addItemToCart(String userId, AddItemToCartRequest request) {

        int quantity = request.getQuantity();
        String productId = request.getProductId();

        if (quantity <= 0) {
            throw new BadApiRequest("Requested quantity is not valid!!!");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with " + productId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not Found " + userId));

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setCartId(UUID.randomUUID().toString());
            newCart.setCreatedAt(new Date());
            newCart.setUser(user);
            return newCart;
        });

        AtomicBoolean updated = new AtomicBoolean(false);
        List<CartItem> items = cart.getItems();

        // Update existing item if it exists
        List<CartItem> updatedItems = items.stream().map(item -> {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getPrice().intValue()); // Fix here
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

        cart.setItems(updatedItems);

        // If the product is not already in the cart, create a new cart item
        if (!updated.get()) {
            CartItem cartItem = new CartItem();
            cartItem.setQuantity(quantity);
            cartItem.setTotalPrice(quantity * product.getPrice().intValue()); // Fix here
            cartItem.setProduct(product);
            cartItem.setCart(cart);

            cart.getItems().add(cartItem);
        }

        Cart save = cartRepository.save(cart);

        return mapper.map(save, CartDTO.class);
    }


    @Override
    public void removeItemFromCart(String userId, int cartItem)  {

        CartItem cartItem1 = cartItemRepository.findById(cartItem)
                .orElseThrow((() -> new BadApiRequest("Cart Not found Exception!!! ")));
        cartItemRepository.delete(cartItem1);

    }

    @Override
    public void clearCart(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow((() -> new UserNotFoundException("User not found Exception !!! " + userId)));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow((() -> new UserNotFoundException("cart of given user is not found!!!")));

        cart.getItems().clear();
        cartRepository.save(cart);

    }

    @Override
    public CartDTO getCartByUser(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow((() -> new UserNotFoundException("User not found Exception !!! " + userId)));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow((() -> new UserNotFoundException("cart of given user is not found!!!")));
        return mapper.map(cart,CartDTO .class);
    }

}
