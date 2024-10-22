package com.BE.model.request;

import lombok.Data;

@Data
public class BookingKoifishDTO {
    private Long farmId;
    private Long koiFishId;
    private Long quantity;
    private Long price;
}
