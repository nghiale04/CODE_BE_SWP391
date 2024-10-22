package com.BE.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KoiRequestDTO {
    private long id;
    private String koiName;
    private String detail;
    private Long price;
    private String image;
    private List<FarmKoiRequestDTO> farmKoiList = new ArrayList<>();
}
