package com.chokhaniyash.fooddeliveryappapi.service;

import com.chokhaniyash.fooddeliveryappapi.entity.OrderEntity;
import com.chokhaniyash.fooddeliveryappapi.io.OrderRequest;
import com.chokhaniyash.fooddeliveryappapi.io.OrderResponse;
import com.chokhaniyash.fooddeliveryappapi.repository.CartRepository;
import com.chokhaniyash.fooddeliveryappapi.repository.OrderRepository;
import com.razorpay.Order;
import com.razorpay.OrderClient;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final CartRepository cartRepository;
    @Value("${razorpay.key}")
    private String RAZORPAY_KEY;
    @Value("${razorpay.secret}")
    private String RAZORPAY_SECRET;

    public OrderServiceImpl(OrderRepository orderRepository, UserService userService, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.cartRepository = cartRepository;
    }

    @Override
    public OrderResponse createOrderWithPayment(OrderRequest request) throws RazorpayException {
        OrderEntity order = convertToEntity(request);
        order = orderRepository.save(order);

        RazorpayClient razorpayClient = new RazorpayClient(RAZORPAY_KEY,RAZORPAY_SECRET);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount",order.getAmount());
        orderRequest.put("currency","INR");
        orderRequest.put("payment_capture",1);
        Order razorpayOrder = razorpayClient.orders.create(orderRequest);

        order.setRazorpayOrderId(razorpayOrder.get("id"));
        String loggedInUser = userService.findByUserId();
        order.setUserId(loggedInUser);
        order = orderRepository.save(order);
        return convertToResponse(order);

    }

    @Override
    public void verifyPayment(Map<String, String> paymentData, String status) {
        String razorpayOrderId = paymentData.get("razorpay_order_id");
        OrderEntity existingOrder = orderRepository.findByRazorpayOrderId(razorpayOrderId).orElseThrow(() -> new RuntimeException("Order not found"));
        existingOrder.setPaymentStatus(status);
        existingOrder.setRazorpaySignature(paymentData.get("razorpay_signature"));
        existingOrder.setRazorpayPaymentId(paymentData.get("razorpay_payment_id"));
        orderRepository.save(existingOrder);
        if("paid".equalsIgnoreCase(status)){
            cartRepository.deleteByUserId(existingOrder.getUserId());
        }
    }

    @Override
    public List<OrderResponse> getUserOrders() {
        String loggedInUserId = userService.findByUserId();
        List<OrderEntity> list = orderRepository.findByUserId(loggedInUserId);
        return list.stream().map(entity -> convertToResponse(entity) ).collect(Collectors.toList());
    }

    @Override
    public void removeOrder(String orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<OrderResponse> getOrdersOfAllUsers() {
        List<OrderEntity> list = orderRepository.findAll();
        return list.stream().map(entity -> convertToResponse(entity) ).collect(Collectors.toList());
    }

    @Override
    public void updateOrderStatus(String orderId, String status) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(()-> new RuntimeException("Order not found"));
        order.setOrderStatus(status);
        orderRepository.save(order);
    }

    private OrderResponse convertToResponse(OrderEntity order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .userAddress(order.getUserAddress())
                .phoneNumber(order.getPhoneNumber())
                .email(order.getEmail())
                .amount(order.getAmount())
                .paymentStatus(order.getPaymentStatus())
                .razorpayOrderId(order.getRazorpayOrderId())
                .email(order.getEmail())
                .phoneNumber(order.getPhoneNumber())
                .orderStatus(order.getOrderStatus())
                .orderedItem(order.getOrderedItem())
                .build();
    }


    private OrderEntity convertToEntity(OrderRequest request){
        return OrderEntity.builder()
                .orderedItem(request.getOrderedItems())
                .userAddress(request.getUserAddress())
                .amount(request.getAmount())
                .email(request.getEmail())
                .orderStatus(request.getOrderStatus())
                .phoneNumber(request.getPhoneNumber())
                .build();
    }

}
