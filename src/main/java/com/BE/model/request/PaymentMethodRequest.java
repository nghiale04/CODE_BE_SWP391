package com.BE.model.request;

import com.BE.enums.PaymentEnum;
import lombok.Data;

@Data
public class PaymentMethodRequest {
    private PaymentEnum paymentMethod;
}
