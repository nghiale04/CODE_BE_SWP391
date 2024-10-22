package com.BE.Converter;

import com.BE.model.entity.FarmKoiEntity;
import com.BE.model.request.FarmKoiRequestDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Converter {
    @Autowired
    private ModelMapper modelMapper;
    public List<FarmKoiRequestDTO> convertFarmKoiToEntity(List<FarmKoiEntity> listFarmKoi) {
        List<FarmKoiRequestDTO> farmKoiRequestDTOList = new ArrayList<>();
        for (FarmKoiEntity farmKoi : listFarmKoi) {
            FarmKoiRequestDTO farmKoiRequestDTO = new FarmKoiRequestDTO();
            farmKoiRequestDTO.setQuantity(farmKoi.getQuantity());
            farmKoiRequestDTO.setKoiId(farmKoi.getKoiFish().getId());
            farmKoiRequestDTO.setFarmId(farmKoi.getFarmKoi().getId());
            farmKoiRequestDTOList.add(farmKoiRequestDTO);
        }
        return farmKoiRequestDTOList;
    }
}
