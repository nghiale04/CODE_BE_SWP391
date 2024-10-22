package com.BE.model.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    List<OrderDetailRequest> detail;
}
