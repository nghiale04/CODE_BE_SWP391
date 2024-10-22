package com.BE.model.request;

import lombok.Data;

@Data
public class FeedbackRequestDTO {
    private int rating;
    private String comment;

}
