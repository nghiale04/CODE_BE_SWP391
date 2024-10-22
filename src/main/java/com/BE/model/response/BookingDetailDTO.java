package com.BE.model.response;

import com.BE.enums.BookingStatusEnum;
import com.BE.model.request.BookingKoifishDTO;
import com.BE.model.request.ProcessingDTO;
import lombok.Data;

import java.util.List;

@Data
public class BookingDetailDTO {
    private long id;
    private long numberOfAdult;
    private long numberOfChild;
    private long totalPrice;
    private BookingStatusEnum status;
    private List<ProcessingDTO> processing;
    private List<BookingKoifishDTO> bookingKoifish;
}
