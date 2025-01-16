package com.red.ElectronicStore.services.Impl;

import com.red.ElectronicStore.Enumeration.OrderStatus;
import com.red.ElectronicStore.dto.OrderDTO;
import com.red.ElectronicStore.dto.PageableResponse;
import com.red.ElectronicStore.entities.*;
import com.red.ElectronicStore.exceptions.BadApiRequest;
import com.red.ElectronicStore.exceptions.CartNotFoundException;
import com.red.ElectronicStore.exceptions.OrderNotFoundException;
import com.red.ElectronicStore.exceptions.UserNotFoundException;
import com.red.ElectronicStore.helper.PageResponseHandler;
import com.red.ElectronicStore.repositories.CartRepository;
import com.red.ElectronicStore.repositories.OrderRepository;
import com.red.ElectronicStore.repositories.UserRepository;
import com.red.ElectronicStore.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CartRepository cartRepository;



    @Override
    public OrderDTO createOrder(OrderDTO orderDTO, String userId,String cartId) {

        User user = userRepository.findById(userId)
                .orElseThrow((() -> new UserNotFoundException("User not found with given id" + userId)));

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow((() -> new CartNotFoundException("Cart is not available" + cartId)));

        List<CartItem> cartItems = cart.getItems();

        if(cartItems.size()<=0){
            throw new BadApiRequest("No items present in cart");
        }



        Order order = Order.builder()
                .orderId(UUID.randomUUID().toString())
                .orderDate(new Date())
//                .orderAmount(orderDTO.getOrderAmount())
                .status(orderDTO.getStatus())
                .customerName(orderDTO.getCustomerName())
                .customerEmail(orderDTO.getCustomerEmail())
                .shippingAddress(orderDTO.getShippingAddress())
                .billingAddress(orderDTO.getBillingAddress())
                .paymentStatus(orderDTO.getPaymentStatus())
//                .orderItems(orderDTO.getOrderItems())
                .deliverDate(orderDTO.getDeliverDate())
                .user(user)
                .build();

        AtomicReference<Double> orderAmount = new AtomicReference<>(0.0);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {

//            OrderItem.builder()
//                    .quantity(cartItem.getQuantity())
//                    .product(cartItem.getProduct())
//                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscount())
//                    .order(order)
//                    .build();

            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setTotalPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice());
            orderItem.setOrder(order);

            orderAmount.set(orderAmount.get()+ orderItem.getTotalPrice());
            return orderItem;


        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());

        cart.getItems().clear();
        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);

        return mapper.map(savedOrder,OrderDTO.class);
    }

    @Override
    public OrderDTO getOrderById(String orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow((() -> new OrderNotFoundException("Order not found")));

        return mapper.map(order,OrderDTO.class);
    }

    @Override
    public PageableResponse<OrderDTO> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        // Fetch all products from the repository
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Order> page = orderRepository.findAll(pageable);
        return PageResponseHandler.getPageableResponse(page,OrderDTO.class);
    }

    @Override
    public OrderDTO updateOrder(String orderId, OrderDTO updatedOrder) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id " + orderId));

        existingOrder.setCustomerName(updatedOrder.getCustomerName());
        existingOrder.setCustomerEmail(updatedOrder.getCustomerEmail());
        existingOrder.setShippingAddress(updatedOrder.getShippingAddress());
        existingOrder.setBillingAddress(updatedOrder.getBillingAddress());
        existingOrder.setPaymentStatus(updatedOrder.getPaymentStatus());
        existingOrder.setStatus(updatedOrder.getStatus());
        existingOrder.setDeliverDate(updatedOrder.getDeliverDate());
        Order updated = orderRepository.save(existingOrder);



        return mapper.map(updated,OrderDTO.class);
    }

    @Override
    public OrderDTO updateOrderStatus(String orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow((() -> new OrderNotFoundException("Order Not found!!")));

        order.setStatus(status);
        return mapper.map(order,OrderDTO.class);
    }

    @Override
    public void deleteOrder(String orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("No Order" + orderId));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow((() -> new UserNotFoundException("User Not found!!!")));
        List<Order> byUser = orderRepository.findByUser(user);
        List<OrderDTO> orderDto = byUser.stream()
                .map(
                        order -> mapper.map(order, OrderDTO.class)
                ).collect(Collectors.toList());

        return orderDto;
    }


}
