package com.BE.service;

import com.BE.model.entity.TourEntity;
import com.BE.model.request.FindTourRequestDTO;
import com.BE.model.request.TourRequestDTO;
import com.BE.model.response.TourResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface TourService {
    TourRequestDTO add(TourRequestDTO tourDTO);
    List<TourRequestDTO> getAll();
    Page<TourResponseDTO> getAllTour(FindTourRequestDTO findTourRequestDTO);
    TourRequestDTO getTourById(Long id);
    TourRequestDTO update(long id, TourRequestDTO tourRequestDTO);
    boolean delete(long id);
}
