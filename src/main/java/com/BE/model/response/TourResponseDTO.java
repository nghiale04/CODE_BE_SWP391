package com.BE.model.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourResponseDTO {
    private long id;
    private String tourName;
    private String decription;
    private Date tourStart;
    private Date tourEnd;
    private long tourPrice;
}
