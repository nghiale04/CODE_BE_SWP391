package com.BE.model.response;

import com.BE.enums.BookingStatusEnum;
import lombok.Data;

@Data
public class BookingResponseDTO {
    private long id;
    private String tourName;
    private long numberOfAdult;
    private long numberOfChild;
    private long totalPrice;
    private BookingStatusEnum status;

}
