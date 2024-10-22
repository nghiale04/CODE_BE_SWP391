package com.BE.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindTourRequestDTO {
    private String nameFarm;
    private String koiType;
    private long price;
    private LocalDate startTime;
    private LocalDate endTime;
    private int pageNumber = 1;
    private int pageSize = 10;
}
