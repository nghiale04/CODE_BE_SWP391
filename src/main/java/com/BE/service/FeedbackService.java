package com.BE.service;


import com.BE.model.request.FeedbackRequestDTO;
import com.BE.model.response.FeedbackResponseDTO;

import java.util.List;

public interface FeedbackService {
    FeedbackRequestDTO addFeedback(long bookingId,FeedbackRequestDTO feedbackRequestDTO);
    List<FeedbackResponseDTO> getAllFeedback(long bookingId);
}
