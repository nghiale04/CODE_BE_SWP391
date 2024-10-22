package com.BE.controller;

import com.BE.model.request.FarmRequestDTO;
import com.BE.model.request.FeedbackRequestDTO;
import com.BE.model.response.FeedbackResponseDTO;
import com.BE.service.FeedbackService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/feedback")
@SecurityRequirement(name ="api")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;
    @PostMapping("{id}")
    public ResponseEntity<FeedbackRequestDTO> addFeedback(@PathVariable long id, @RequestBody FeedbackRequestDTO feedback){
        FeedbackRequestDTO feedbackDTO = feedbackService.addFeedback(id,feedback);
        return ResponseEntity.ok(feedbackDTO);
    }

    @GetMapping("{id}")
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedback(@PathVariable long id){
        List<FeedbackResponseDTO> listFeedback = feedbackService.getAllFeedback(id);
        return ResponseEntity.ok(listFeedback);
    }


}
