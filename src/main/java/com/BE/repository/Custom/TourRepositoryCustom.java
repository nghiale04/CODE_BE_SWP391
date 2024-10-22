package com.BE.repository.Custom;

import com.BE.model.entity.TourEntity;
import com.BE.model.request.FindTourRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TourRepositoryCustom {
    Page<TourEntity> findAllTour(FindTourRequestDTO findTourRequestDTO, Pageable pageable);
}
