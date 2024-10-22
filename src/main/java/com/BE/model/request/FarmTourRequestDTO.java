package com.BE.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FarmTourRequestDTO {
    private String description;
    private int farmId;
    private int tourId;
}
