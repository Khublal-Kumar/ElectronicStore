package com.red.ElectronicStore.controllers;

import com.red.ElectronicStore.Enumeration.OrderStatus;
import com.red.ElectronicStore.dto.OrderDTO;
import com.red.ElectronicStore.dto.PageableResponse;
import com.red.ElectronicStore.helper.ApiResponseMessage;
import com.red.ElectronicStore.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Create a new order
    @PostMapping("/create/{userId}/{cartId}")
    public ResponseEntity<OrderDTO> createOrder(
            @RequestBody OrderDTO orderDTO,
            @PathVariable String userId,
            @PathVariable String cartId) {
        OrderDTO createdOrder = orderService.createOrder(orderDTO, userId, cartId);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    // Get order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable String orderId) {
        OrderDTO order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    // Get all orders with pagination and sorting
    @GetMapping
    public ResponseEntity<PageableResponse<OrderDTO>> getOrders(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        PageableResponse<OrderDTO> orders = orderService.getOrders(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(orders);
    }

    // Update an order
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDTO> updateOrder(
            @PathVariable String orderId,
            @RequestBody OrderDTO updatedOrder) {
        OrderDTO order = orderService.updateOrder(orderId, updatedOrder);
        return ResponseEntity.ok(order);
    }

    // Update order status
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam OrderStatus status) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }

    // Delete an order
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> deleteOrder(@PathVariable String orderId) {
        orderService.deleteOrder(orderId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("Order Deleted!!!")
                .status(HttpStatus.BAD_REQUEST)
                .success(true)
                .build();
        return new ResponseEntity<>(apiResponseMessage,HttpStatus.OK);
    }

    // Get orders by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable String userId) {
        List<OrderDTO> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }
}
