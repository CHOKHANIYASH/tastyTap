package com.chokhaniyash.fooddeliveryappapi.controller;

import com.chokhaniyash.fooddeliveryappapi.io.OrderRequest;
import com.chokhaniyash.fooddeliveryappapi.io.OrderResponse;
import com.chokhaniyash.fooddeliveryappapi.service.OrderService;
import com.razorpay.RazorpayException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrderWithPayment(@RequestBody OrderRequest request) throws RazorpayException {
        return orderService.createOrderWithPayment(request);
    }
    @GetMapping("/verify")
    public void verifyPayment(@RequestBody Map<String,String> paymentData){
        orderService.verifyPayment(paymentData,"Paid");
    }
    @GetMapping
    public List<OrderResponse> getOrders(){
        return orderService.getUserOrders();
    }
    @GetMapping("/all")
    public List<OrderResponse> getOrdersOfAllUsers(){
        return orderService.getOrdersOfAllUsers();
    }
    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable("orderId") String orderId){
        orderService.removeOrder(orderId);
    }
    @PatchMapping("/status/{orderId}")
    public void updateOrderStatus(@PathVariable String orderId,@RequestParam String status){
        orderService.updateOrderStatus(orderId,status);
    }
}
