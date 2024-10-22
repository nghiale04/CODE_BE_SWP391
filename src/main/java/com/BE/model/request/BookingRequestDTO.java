package com.BE.model.request;

import lombok.Data;

@Data
public class  BookingRequestDTO {
    private long tourId;
    private long numberOfAdult;
    private long numberOfChild;
}
