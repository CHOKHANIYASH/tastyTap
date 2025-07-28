package com.chokhaniyash.fooddeliveryappapi.service;

import com.chokhaniyash.fooddeliveryappapi.io.OrderRequest;
import com.chokhaniyash.fooddeliveryappapi.io.OrderResponse;
import com.razorpay.RazorpayException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface OrderService {
    public OrderResponse createOrderWithPayment(OrderRequest request) throws RazorpayException;

    void verifyPayment(Map<String,String> paymentData, String status);

    List<OrderResponse> getUserOrders();

    void removeOrder(String orderId);

    List<OrderResponse> getOrdersOfAllUsers();

    void updateOrderStatus(String orderId, String status);
}
