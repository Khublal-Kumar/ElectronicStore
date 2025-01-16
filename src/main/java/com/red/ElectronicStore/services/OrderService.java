package com.red.ElectronicStore.services;

import com.red.ElectronicStore.Enumeration.OrderStatus;
import com.red.ElectronicStore.dto.OrderDTO;
import com.red.ElectronicStore.dto.PageableResponse;
import com.red.ElectronicStore.entities.Order;

import java.util.List;

public interface OrderService {

    // Create a new order
    OrderDTO createOrder(OrderDTO orderDTO,String userId, String cartId);

    // Retrieve an order by its ID
    OrderDTO getOrderById(String orderId);

    // Retrieve all orders
    PageableResponse<OrderDTO> getOrders(int pageNumber,int pageSize, String SortBy,String sortDir);

    // Update order details
    OrderDTO updateOrder(String orderId, OrderDTO updatedOrder);

    // Update the status of an order
    OrderDTO updateOrderStatus(String orderId, OrderStatus status);

    // Delete an order by its ID
    void deleteOrder(String orderId);

    // Retrieve orders by user ID
    List<OrderDTO> getOrdersByUserId(String userId);

    // Calculate total revenue
}
