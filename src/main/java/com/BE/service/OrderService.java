package com.BE.service;

import com.BE.model.entity.OrderEntity;
import com.BE.model.entity.User;
import com.BE.model.request.OrderRequestDTO;
import com.BE.model.request.PaymentMethodRequest;

import java.util.List;

public interface OrderService {
     List<OrderEntity> getListOrder();
     String createAndCheckMethod(long bookingId, PaymentMethodRequest paymentMethodRequest) throws Exception;
     void createTransaction(Long id);
}
