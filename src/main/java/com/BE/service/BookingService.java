package com.BE.service;


import com.BE.model.request.*;
import com.BE.model.response.BookingDetailDTO;
import com.BE.model.response.BookingResponseDTO;

import java.util.List;

public interface BookingService {
    BookingRequestDTO createBooking(BookingRequestDTO bookingRequestDTO);
    List<BookingResponseDTO> getAllBookingByUser();
    List<BookingResponseDTO> getAllBooking();
    BookingDetailDTO getDetailBooking(Long id);
    String censor(long id, StatusRequest status);
    void updateProcessingStatus(Long id, ProcessingRequestDTO processing);
    void addKoifishBooking (Long id, BookingKoiFishRequest bookingKoifish);
    boolean cancelBooking(Long id);
}
