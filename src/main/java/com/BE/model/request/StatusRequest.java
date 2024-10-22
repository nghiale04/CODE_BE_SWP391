package com.BE.model.request;

import com.BE.enums.BookingStatusEnum;
import lombok.Data;

@Data
public class StatusRequest {
    private BookingStatusEnum status;
}
