package com.BE.service.impl;

import com.BE.Converter.Converter;
import com.BE.model.entity.FarmEntity;
import com.BE.model.entity.FarmKoiEntity;
import com.BE.model.entity.FarmTourEntity;
import com.BE.model.request.FarmKoiRequestDTO;
import com.BE.model.request.FarmRequestDTO;
import com.BE.model.request.FarmTourRequestDTO;
import com.BE.repository.*;
import com.BE.service.FarmService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FarmServiceImpl implements FarmService {


    @Autowired
    private FarmRepository farmRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private KoiFishRepository koiFishRepository;
    @Autowired
    private TourRepository tourRepository;
    @Autowired
    private FarmKoiRepository farmKoiRepository;
    @Autowired
    private FarmTourRepository farmTourRepository;
    @Autowired
    private Converter converter;
    @Override
    public FarmRequestDTO add(FarmRequestDTO farmRequestDTO) {
        // Chuyển đổi DTO sang Entity
        FarmEntity farmEntity = modelMapper.map(farmRequestDTO, FarmEntity.class);

        // Xử lý Farm Koi
        List<FarmKoiEntity> farmKoiEntities = farmRequestDTO.getListFarmKoi().stream().map(koiRequestDTO -> {
            FarmKoiEntity farmKoiEntity = new FarmKoiEntity();
            farmKoiEntity.setQuantity(koiRequestDTO.getQuantity());
            farmKoiEntity.setKoiFish(koiFishRepository.findById(koiRequestDTO.getKoiId())
                    .orElseThrow(() -> new EntityNotFoundException("Koi not found!")));
            farmKoiEntity.setFarmKoi(farmEntity);
            return farmKoiEntity;
        }).collect(Collectors.toList());

        // Xử lý Farm Tour
        List<FarmTourEntity> farmTourEntities = farmRequestDTO.getListFarmTour().stream().map(tourRequestDTO -> {
            FarmTourEntity farmTourEntity = new FarmTourEntity();
            farmTourEntity.setDescription(tourRequestDTO.getDescription());
            farmTourEntity.setTour(tourRepository.findById(tourRequestDTO.getTourId())
                    .orElseThrow(() -> new EntityNotFoundException("Tour not found!")));
            farmTourEntity.setFarm(farmEntity);
            return farmTourEntity;
        }).collect(Collectors.toList());

        // Gán danh sách cho farmEntity
        farmEntity.setFarmKoisEntities(farmKoiEntities);
        farmEntity.setFarmTourEntities(farmTourEntities);

        // Lưu FarmEntity
        FarmEntity savedFarm = farmRepository.save(farmEntity);
        return modelMapper.map(savedFarm, FarmRequestDTO.class);
    }

    @Override
    public List<FarmRequestDTO> getAllFarm() {
        return farmRepository.findAll().stream()
                .map(farmEntity -> modelMapper.map(farmEntity, FarmRequestDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public FarmRequestDTO update(long id, FarmRequestDTO farmRequestDTO) {
        FarmEntity farmEntity = farmRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Farm not found!"));

        // Cập nhật thông tin Farm
        modelMapper.map(farmRequestDTO, farmEntity);

        // Xử lý Farm Koi
        List<FarmKoiEntity> existingKois = farmKoiRepository.findAllByFarmKoi(farmEntity);
        farmKoiRepository.deleteAll(existingKois);

        List<FarmKoiEntity> newFarmKoiList = farmRequestDTO.getListFarmKoi().stream().map(koiRequestDTO -> {
            FarmKoiEntity farmKoiEntity = new FarmKoiEntity();
            farmKoiEntity.setQuantity(koiRequestDTO.getQuantity());
            farmKoiEntity.setKoiFish(koiFishRepository.findById(koiRequestDTO.getKoiId())
                    .orElseThrow(() -> new EntityNotFoundException("Koi not found!")));
            farmKoiEntity.setFarmKoi(farmEntity);
            return farmKoiEntity;
        }).collect(Collectors.toList());

        // Xử lý Farm Tour
        List<FarmTourEntity> existingTours = farmTourRepository.findAllByFarm(farmEntity);
        farmTourRepository.deleteAll(existingTours);

        List<FarmTourEntity> newFarmTourList = farmRequestDTO.getListFarmTour().stream().map(tourRequestDTO -> {
            FarmTourEntity farmTourEntity = new FarmTourEntity();
            farmTourEntity.setDescription(tourRequestDTO.getDescription());
            farmTourEntity.setTour(tourRepository.findById(tourRequestDTO.getTourId())
                    .orElseThrow(() -> new EntityNotFoundException("Tour not found!")));
            farmTourEntity.setFarm(farmEntity);
            return farmTourEntity;
        }).collect(Collectors.toList());

        // Gán lại danh sách
        farmEntity.setFarmKoisEntities(newFarmKoiList);
        farmEntity.setFarmTourEntities(newFarmTourList);

        // Lưu FarmEntity
        FarmEntity updatedFarm = farmRepository.save(farmEntity);
        return modelMapper.map(updatedFarm, FarmRequestDTO.class);
    }


    @Override
    public boolean deleteFarm(long id) {
        Optional<FarmEntity> farm = farmRepository.findById(id);
        if (farm.isPresent()) {
            FarmEntity farmEntity = farm.get();
            farmRepository.delete(farmEntity);
            return true;
        }
        return false;
    }

    @Override
    public FarmRequestDTO getFarm(long id) {
        FarmEntity farmEntity = farmRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Farm not found!"));
        FarmRequestDTO farmRequestDTO = modelMapper.map(farmEntity, FarmRequestDTO.class);
        farmRequestDTO.setListFarmKoi(converter.convertFarmKoiToEntity(farmEntity.getFarmKoisEntities()));
        return farmRequestDTO;
    }
}
