package com.red.ElectronicStore.dto;

import com.red.ElectronicStore.Enumeration.OrderStatus;
import com.red.ElectronicStore.Enumeration.PaymentStatus;
import com.red.ElectronicStore.entities.OrderItem;
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
public class OrderDTO {

    private String orderId;

    private Date orderDate= new Date();

    private Double orderAmount;

    private OrderStatus status = OrderStatus.PENDING;

    private String customerName;

    private String customerEmail;

    private String shippingAddress;

    private String billingAddress;

    private PaymentStatus paymentStatus =PaymentStatus.PENDING;

    private List<OrderItemDTO> orderItems = new ArrayList<>();

    private Date deliverDate;

}
