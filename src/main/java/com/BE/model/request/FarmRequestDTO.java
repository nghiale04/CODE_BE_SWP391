package com.BE.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FarmRequestDTO {
    private int id;
    private String farmName;
    private String location;
    private String description;
    private Time startTime;
    private Time endTime;
    private String image;
    private List<FarmTourRequestDTO> listFarmTour = new ArrayList<>();
    private List<FarmKoiRequestDTO> listFarmKoi = new ArrayList<>();
}
