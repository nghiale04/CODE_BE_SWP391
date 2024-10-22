package com.BE.model.request;

import lombok.Data;

import java.util.List;

@Data
public class BookingKoiFishRequest {
   private List<BookingKoifishDTO> bookingKoifish;
}
