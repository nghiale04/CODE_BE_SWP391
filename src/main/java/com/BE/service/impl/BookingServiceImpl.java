package com.BE.service.impl;

import com.BE.enums.BookingStatusEnum;
import com.BE.exception.exceptions.BadRequestException;
import com.BE.model.entity.*;
import com.BE.model.request.*;
import com.BE.model.response.BookingDetailDTO;
import com.BE.model.response.BookingResponseDTO;
import com.BE.repository.*;
import com.BE.service.BookingService;
import com.BE.utils.AccountUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private AccountUtils accountUtils;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private TourRepository tourRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FarmKoiRepository farmKoiRepository;
    @Autowired
    private FarmRepository farmRepository;
    @Autowired
    private KoiFishRepository koiFishRepository;
    @Autowired
    private BookingKoifishRepository bookingKoifishRepository;

    @Override
    public BookingRequestDTO createBooking(BookingRequestDTO bookingRequestDTO) {
        User currentUser = accountUtils.getCurrentUser();
        TourEntity tourEntity = tourRepository.findById(bookingRequestDTO.getTourId()).get();
        Long total = bookingRequestDTO.getNumberOfAdult() * tourEntity.getPriceAdult() + bookingRequestDTO.getNumberOfChild() * tourEntity.getPriceChild();
        BookingEntity bookingEntity = BookingEntity.builder()
                .numberOfAdult(bookingRequestDTO.getNumberOfAdult())
                .numberOfChild(bookingRequestDTO.getNumberOfChild())
                .tour(tourEntity)
                .user(currentUser)
                .totalPrice(total)
                .dateTime(new Date())
                .status(BookingStatusEnum.PENDING)
                .build();

        List<ProcessingEntity> listProcessing = new ArrayList<>();
        ProcessingEntity process1 = new ProcessingEntity();
        process1.setBooking(bookingEntity);
        process1.setType("Consultation and Order Confirmation");
        process1.setStatus(0);
        process1.setDescription(null);
        listProcessing.add(process1);

        ProcessingEntity process2 = new ProcessingEntity();
        process2.setBooking(bookingEntity);
        process2.setType("Order Processing and Delivery Scheduling");
        process2.setStatus(0);
        process2.setDescription(null);
        listProcessing.add(process2);

        ProcessingEntity process3 = new ProcessingEntity();
        process3.setBooking(bookingEntity);
        process3.setType("Pre-Delivery Confirmation");
        process3.setStatus(0);
        process3.setDescription(null);
        listProcessing.add(process3);

        ProcessingEntity process4 = new ProcessingEntity();
        process4.setBooking(bookingEntity);
        process4.setType("Delivery and Final Payment Completion");
        process4.setStatus(0);
        process4.setDescription(null);
        listProcessing.add(process4);

        bookingEntity.setProcessing(listProcessing);

        bookingEntity = bookingRepository.save(bookingEntity);
        return modelMapper.map(bookingEntity, BookingRequestDTO.class);
    }

    @Override
    public String censor(long id, StatusRequest status) {
        BookingEntity bookingEntity = bookingRepository.findById(id).orElseThrow(() -> new BadRequestException("Booking not found"));
        bookingEntity.setStatus(status.getStatus());
        bookingRepository.save(bookingEntity);
        return "Success";
    }

    @Override
    public void updateProcessingStatus(Long bookingId, ProcessingRequestDTO processing) {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        List<ProcessingEntity> processingList = booking.getProcessing();

        // Map dữ liệu cập nhật từ DTO vào các entity có sẵn
        for (ProcessingDTO updateDTO : processing.getProcessingList()) {
            for (ProcessingEntity entity : processingList) {
                if (entity.getType().equals(updateDTO.getType())) {
                    entity.setStatus(updateDTO.getStatus());
                    entity.setDescription(updateDTO.getDescription());
                    break;
                }
            }
        }

        bookingRepository.save(booking);
    }

    @Override
    public void addKoifishBooking(Long bookingId, BookingKoiFishRequest bookingKoifish) {
        List<BookingKoiFishEntity> bookingKoiFishList = new ArrayList<>();
        for (BookingKoifishDTO item : bookingKoifish.getBookingKoifish()){
            BookingKoiFishEntity bookingKoiFishEntity = new BookingKoiFishEntity();
            bookingKoiFishEntity.setBooking(bookingRepository.findById(bookingId).orElseThrow(() -> new BadRequestException("Booking not found!")));
            bookingKoiFishEntity.setQuantity(item.getQuantity());
            bookingKoiFishEntity.setPricePerUnit(item.getPrice());
            bookingKoiFishEntity.setTotalPrice(item.getQuantity()*item.getPrice());
            FarmKoiEntity farmKoiEntity =farmKoiRepository.findByFarmKoiAndKoiFish(farmRepository.findById(item.getFarmId()).get(),koiFishRepository.findById(item.getKoiFishId()).get());
            if (farmKoiEntity == null) {
                throw new BadRequestException("Farm or Koi Fish not found!");
            }
            bookingKoiFishEntity.setFarmKoiEntity(farmKoiEntity);
            bookingKoiFishList.add(bookingKoiFishEntity);
        }
        bookingKoifishRepository.saveAll(bookingKoiFishList);
    }

    @Override
    public boolean cancelBooking(Long id) {
       BookingEntity bookingEntity = bookingRepository.findById(id).orElseThrow(() -> new BadRequestException("Booking not found"));
       if (bookingEntity.getStatus() == BookingStatusEnum.PENDING) {
            bookingRepository.delete(bookingEntity);
            return true;
       }else{
           throw new BadRequestException("Booking is booked cannot cancel!");
       }
    }

    @Override
    public List<BookingResponseDTO> getAllBookingByUser() {
        User currentUser = accountUtils.getCurrentUser();
        List<BookingEntity> bookingEntities = bookingRepository.findAllBookingEntityByUser(currentUser);
        List<BookingResponseDTO> bookingResponseDTO = new ArrayList<>();
        for (BookingEntity bookingEntity : bookingEntities) {
            BookingResponseDTO responseDTO = modelMapper.map(bookingEntity, BookingResponseDTO.class);
            responseDTO.setTourName(bookingEntity.getTour().getTourName());
            bookingResponseDTO.add(responseDTO);
        }
        return bookingResponseDTO;
    }

    @Override
    public List<BookingResponseDTO> getAllBooking() {
        List<BookingEntity> bookingEntities = bookingRepository.findAll();
        List<BookingResponseDTO> bookingResponseDTO = new ArrayList<>();
        for (BookingEntity bookingEntity : bookingEntities) {
            BookingResponseDTO responseDTO = modelMapper.map(bookingEntity, BookingResponseDTO.class);
            bookingResponseDTO.add(responseDTO);
        }
        return bookingResponseDTO;
    }

    @Override
    public BookingDetailDTO getDetailBooking(Long id) {
       BookingEntity booking = bookingRepository.findById(id).orElseThrow(() -> new BadRequestException("Booking not found"));
       if (booking.getStatus() != BookingStatusEnum.APPROVED) {
           throw new BadRequestException("Booking is not approved");
       }
        return modelMapper.map(booking, BookingDetailDTO.class);
    }
}
