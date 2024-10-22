package com.BE.service;

import com.BE.model.entity.KoiFishEntity;
import com.BE.model.request.KoiRequestDTO;

import java.util.List;

public interface KoiFishService {
    List<KoiRequestDTO> getAllKoiFish();
    KoiFishEntity add(KoiRequestDTO KoiRequestDTO);
    boolean update(long id, KoiRequestDTO d);
    boolean delete(long id);
    KoiRequestDTO findById(long id);
}
