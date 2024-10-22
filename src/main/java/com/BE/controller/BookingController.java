package com.BE.controller;

import com.BE.model.request.*;
import com.BE.model.response.BookingDetailDTO;
import com.BE.model.response.BookingResponseDTO;
import com.BE.service.BookingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name ="api")
@RequestMapping("/api/booking")
public class BookingController {
    @Autowired
    private BookingService bookingService;
    @PostMapping
    public ResponseEntity<BookingRequestDTO> createBooking(@RequestBody BookingRequestDTO bookingRequestDTO) {
        BookingRequestDTO createdBooking = bookingService.createBooking(bookingRequestDTO);
        return ResponseEntity.ok().body(createdBooking);
    }
    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAllBookingByUser() {
        List<BookingResponseDTO> allBookings = bookingService.getAllBookingByUser();
        return ResponseEntity.ok().body(allBookings);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity censorBooking(@PathVariable int id, @RequestBody StatusRequest status) {
        return ResponseEntity.ok().body(bookingService.censor(id,status));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<BookingResponseDTO>> getBooking() {
        List<BookingResponseDTO> allBookings = bookingService.getAllBooking();
        return ResponseEntity.ok().body(allBookings);
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('CONSULTANT_STAFF')")
    @PutMapping("{id}/processing")
    public  ResponseEntity updateStatus(@PathVariable long id, @RequestBody ProcessingRequestDTO processing) {
            bookingService.updateProcessingStatus(id, processing);
            return ResponseEntity.ok("Processing status updated successfully.");
    }

    @PutMapping("{id}/koifish")
    public ResponseEntity addKoifishBooking (@PathVariable long id, @RequestBody BookingKoiFishRequest bookingKoifish) {
        bookingService.addKoifishBooking(id, bookingKoifish);
        return ResponseEntity.ok("Add KoiFish to Booking successfully.");
    }


    @GetMapping("{id}")
    public ResponseEntity<BookingDetailDTO> getBookingById(@PathVariable long id) {
        BookingDetailDTO detailBooking = bookingService.getDetailBooking(id);
        return ResponseEntity.ok().body(detailBooking);
    }

    @DeleteMapping("{id}")
    public ResponseEntity cancelBooking(@PathVariable long id) {
        boolean cancelled = bookingService.cancelBooking(id);
        return ResponseEntity.ok().body(cancelled);
    }

}
