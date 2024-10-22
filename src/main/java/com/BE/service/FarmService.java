package com.BE.service;

import com.BE.model.request.FarmRequestDTO;


import java.util.List;

public interface FarmService {
     FarmRequestDTO add(FarmRequestDTO farmRequestDTO);
     List<FarmRequestDTO> getAllFarm();
     FarmRequestDTO update(long id, FarmRequestDTO farmRequestDTO);
     boolean deleteFarm(long id);
     FarmRequestDTO getFarm(long id);
}
