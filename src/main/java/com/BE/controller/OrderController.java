package com.BE.controller;

import com.BE.model.entity.OrderEntity;
import com.BE.model.request.OrderRequestDTO;
import com.BE.model.request.PaymentMethodRequest;
import com.BE.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/order")
@SecurityRequirement(name ="api")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @PostMapping
    public ResponseEntity addOrder(@RequestParam long bookingId, @RequestBody PaymentMethodRequest paymentMethodRequest) throws Exception {
        String vnPayUrl = orderService.createAndCheckMethod(bookingId, paymentMethodRequest);
        return ResponseEntity.ok(vnPayUrl);
    }

    @GetMapping
    public ResponseEntity getOrders() {
        List<OrderEntity> listOrder =  orderService.getListOrder();
        return ResponseEntity.ok(listOrder);
    }


    @PostMapping("transaction")
    public ResponseEntity transaction(@RequestParam long orderId) throws Exception {
        orderService.createTransaction(orderId);
        return ResponseEntity.ok("success");
    }
}
