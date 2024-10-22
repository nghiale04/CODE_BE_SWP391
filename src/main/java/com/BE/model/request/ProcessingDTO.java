package com.BE.model.request;

import lombok.Data;

@Data
public class ProcessingDTO {
    private String type;
    private int status;
    private String description;
}
