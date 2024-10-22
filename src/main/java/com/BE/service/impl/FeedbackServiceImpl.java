package com.BE.service.impl;

import com.BE.model.entity.FeedbackEntity;
import com.BE.model.request.FeedbackRequestDTO;
import com.BE.model.response.FeedbackResponseDTO;
import com.BE.repository.BookingRepository;
import com.BE.repository.FeedbackRepository;
import com.BE.service.FeedbackService;
import com.BE.utils.AccountUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private AccountUtils accountUtils;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public FeedbackRequestDTO addFeedback(long bookingId,FeedbackRequestDTO feedbackRequestDTO) {
        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setBooking(bookingRepository.findById(bookingId).get());
        feedbackEntity.setUser(accountUtils.getCurrentUser());
        feedbackEntity.setRating(feedbackRequestDTO.getRating());
        feedbackEntity.setComment(feedbackRequestDTO.getComment());
        feedbackEntity.setDate(new Date());
        feedbackEntity = feedbackRepository.save(feedbackEntity);
        return modelMapper.map(feedbackEntity, FeedbackRequestDTO.class);
    }

    @Override
    public List<FeedbackResponseDTO> getAllFeedback(long bookingId) {
        List<FeedbackEntity> feedbacks = feedbackRepository.findAllByBooking(bookingRepository.findById(bookingId).get());
        List<FeedbackResponseDTO> feedbackResponseDTOS = new ArrayList<>();
        for (FeedbackEntity feedbackEntity : feedbacks) {
            FeedbackResponseDTO feedbackResponseDTO = modelMapper.map(feedbackEntity, FeedbackResponseDTO.class);
            feedbackResponseDTO.setName(feedbackEntity.getUser().getFullName());
            feedbackResponseDTO.setDate(feedbackEntity.getDate());
            feedbackResponseDTOS.add(feedbackResponseDTO);
        }
        return feedbackResponseDTOS;
    }
}
