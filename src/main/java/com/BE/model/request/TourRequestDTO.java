package com.BE.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourRequestDTO {
    private long id;
    private String tourName;
    private String decription;
    private Date tourStart;
    private Date tourEnd;
    private Long priceAdult;
    private Long priceChild;
    private Long recipients;
    private String image;
    private List<FarmTourRequestDTO> listFarmTour = new ArrayList<>();

}
