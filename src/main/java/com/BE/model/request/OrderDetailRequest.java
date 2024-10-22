package com.BE.model.request;

import lombok.Data;

@Data
public class OrderDetailRequest {
    private long koiId;
    private long quantity;
}
