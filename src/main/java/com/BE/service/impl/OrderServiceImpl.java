package com.BE.service.impl;

import com.BE.enums.BookingStatusEnum;
import com.BE.enums.PaymentEnum;
import com.BE.enums.RoleEnum;
import com.BE.enums.TransactionEnum;
import com.BE.exception.exceptions.BadRequestException;
import com.BE.exception.exceptions.NotFoundException;
import com.BE.model.entity.*;
import com.BE.model.request.PaymentMethodRequest;
import com.BE.repository.*;
import com.BE.service.OrderService;
import com.BE.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AccountUtils accountUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    public OrderEntity addOrder(long bookingId) throws BadRequestException {

        User currentUser = accountUtils.getCurrentUser();
        BookingEntity booking = bookingRepository.findById(bookingId).get();
        if (booking.getStatus() != BookingStatusEnum.APPROVED){
            throw new BadRequestException("Booking is not approved!");
        }else{
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setDate(new Date());
            orderEntity.setCustomer(currentUser);
            orderEntity.setBooking(booking);
            orderEntity.setTotal(booking.getTotalPrice());
            return orderRepository.save(orderEntity);
        }


    }
    @Override
    public String createAndCheckMethod(long bookingId, PaymentMethodRequest paymentMethodRequest) throws Exception {
        String response;
        if (paymentMethodRequest.getPaymentMethod().equals(PaymentEnum.BANKING)){
            response = createURL(bookingId);
            PaymentEntity payment = new PaymentEntity();
            OrderEntity order = addOrder(bookingId);
            payment.setOrder(order);
            payment.setDate(new Date());
            payment.setPaymentMethod(PaymentEnum.BANKING);
            paymentRepository.save(payment);
        }else{
            OrderEntity order = addOrder(bookingId);
            PaymentEntity payment = new PaymentEntity();
            payment.setOrder(order);
            payment.setDate(new Date());
            payment.setPaymentMethod(PaymentEnum.CASH);
            paymentRepository.save(payment);
            response = "Your order has been created successfully!";
        }
        return response;
    }

    @Override
    public List<OrderEntity> getListOrder() {
        User currentUser = accountUtils.getCurrentUser();
        return  orderRepository.findOrderEntityByCustomer(currentUser);
    }

    public String createURL(long bookingId) throws Exception{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime createDate = LocalDateTime.now();
        String formattedCreateDate = createDate.format(formatter);

        OrderEntity orderEntity = addOrder(bookingId);
        float money = orderEntity.getTotal() *100;
        String amount = String.valueOf((int)money);


        String tmnCode = "OGCJ2OLK";
        String secretKey = "9XCXI7UJLMWZWBHPDG65OKB6IRDB0DZZ";
        String vnpUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String returnUrl = "https://www.behance.net/search/projects?orderID=" + orderEntity.getId();
        String currCode = "VND";

        Map<String, String> vnp_Params = new TreeMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", tmnCode);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_CurrCode", currCode);
        vnp_Params.put("vnp_TxnRef", String.valueOf(orderEntity.getId()));
        vnp_Params.put("vnp_OrderInfo", "Thanh toan cho ma giao dich: " +orderEntity.getId());
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Amount", amount);

        vnp_Params.put("vnp_ReturnUrl",returnUrl);
        vnp_Params.put("vnp_CreateDate", formattedCreateDate);
        vnp_Params.put("vnp_IpAddr", "14.225.204.58");

        StringBuilder signDataBuilder = new StringBuilder();
        for (Map. Entry<String, String> entry: vnp_Params.entrySet()) {
            signDataBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
            signDataBuilder.append("=");
            signDataBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            signDataBuilder.append("&");
        }
        signDataBuilder.deleteCharAt(signDataBuilder.length() - 1);
        String signData = signDataBuilder.toString();
        String signed = generateHMAC(secretKey, signData);
        vnp_Params.put("vnp_SecureHash", signed);

        StringBuilder urlBuilder = new StringBuilder (vnpUrl);
        urlBuilder.append("?");
        for (Map. Entry<String, String> entry: vnp_Params.entrySet()) {
            urlBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
            urlBuilder.append("=");
            urlBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            urlBuilder.append("&");
        }
            urlBuilder.deleteCharAt(urlBuilder.length()-1);
            return urlBuilder.toString();
    }



    private String generateHMAC(String secretKey, String signData) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSha512 =  Mac.getInstance( "HmacSHA512");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes (StandardCharsets.UTF_8), "HmacSHA512"); hmacSha512.init(keySpec);
        byte[] hmacBytes = hmacSha512.doFinal(signData.getBytes(StandardCharsets.UTF_8));
        StringBuilder result = new StringBuilder();
        for (byte b: hmacBytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    @Override
    public void  createTransaction(Long id) {
        OrderEntity order = orderRepository.findById(id).orElseThrow(()->new NotFoundException("Order Not Found"));

        //Tao payment
        PaymentEntity payment = new PaymentEntity();
        payment.setOrder(order);
        payment.setDate(new Date());
        payment.setPaymentMethod(PaymentEnum.BANKING);
        paymentRepository.save(payment);

    }
}
