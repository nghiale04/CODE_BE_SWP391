package com.BE.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class FeedbackResponseDTO {
    private String name;
    private String rating;
    private String comment;
    private Date date;
}
