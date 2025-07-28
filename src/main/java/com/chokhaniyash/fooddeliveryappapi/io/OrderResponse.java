package com.chokhaniyash.fooddeliveryappapi.io;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderResponse {
    private String id;
    private String userId;
    private String userAddress;
    private String phoneNumber;
    private String email;
    private List<OrderItem> orderedItem;
    private double amount;
    private String paymentStatus;
    private String razorpayOrderId;
    private String orderStatus;
}
