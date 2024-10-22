package com.BE.service.impl;

import com.BE.model.entity.FarmEntity;
import com.BE.model.entity.FarmTourEntity;
import com.BE.model.entity.Response;
import com.BE.model.entity.TourEntity;
import com.BE.model.request.FarmTourRequestDTO;
import com.BE.model.request.FindTourRequestDTO;
import com.BE.model.request.TourRequestDTO;
import com.BE.model.response.TourResponseDTO;
import com.BE.repository.FarmRepository;
import com.BE.repository.FarmTourRepository;
import com.BE.repository.TourRepository;
import com.BE.service.TourService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TourServiceImpl implements TourService {
    @Autowired
    private TourRepository tourRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FarmRepository farmRepository;
    @Autowired
    private FarmTourRepository farmTourRepository;

    @Override
    public TourRequestDTO add(TourRequestDTO tourRequestDTO) {
        TourEntity tourEntity = modelMapper.map(tourRequestDTO, TourEntity.class);
        List<FarmTourRequestDTO> farmTourRequestDTOS = tourRequestDTO.getListFarmTour();
        List<FarmTourEntity> farmTourEntities = new ArrayList<>();
        for (FarmTourRequestDTO item : farmTourRequestDTOS) {
            FarmTourEntity farmTourEntity = new FarmTourEntity();
            farmTourEntity.setDescription(item.getDescription());
            farmTourEntity.setFarm(farmRepository.findById(item.getFarmId()).orElseThrow(() -> new EntityNotFoundException("Farm not found!")));
            farmTourEntity.setTour(tourEntity);
            farmTourEntities.add(farmTourEntity);
        }
        tourEntity.setFarmTourEntities(farmTourEntities);
        TourEntity responeTour = tourRepository.save(tourEntity);
        TourRequestDTO tourResponseDTO = modelMapper.map(responeTour, TourRequestDTO.class);
        return tourResponseDTO;
    }

    @Override
    public List<TourRequestDTO> getAll() {
        List<TourEntity> listTour = tourRepository.findAll();
        List<TourRequestDTO> tourRequestDTOS = new ArrayList<>();
        for (TourEntity item : listTour) {
            TourRequestDTO tourRequestDTO = modelMapper.map(item, TourRequestDTO.class);
            tourRequestDTOS.add(tourRequestDTO);
        }
        return tourRequestDTOS;
    }


    @Override
    public Page<TourResponseDTO> getAllTour(FindTourRequestDTO findTourRequestDTO) {
        // Tạo đối tượng Pageable từ request DTO
        Pageable pageable = PageRequest.of(findTourRequestDTO.getPageNumber() - 1, findTourRequestDTO.getPageSize());

        Page<TourEntity> tourEntities = tourRepository.findAllTour(findTourRequestDTO, pageable);
        List<TourResponseDTO> tourResponseDTOS = new ArrayList<>();

        for (TourEntity item : tourEntities) {
            TourResponseDTO tourResponseDTO = new TourResponseDTO();
            tourResponseDTO.setId(item.getId());
            tourResponseDTO.setTourName(item.getTourName());
            tourResponseDTO.setDecription(item.getDecription());
            tourResponseDTO.setTourStart(item.getTourStart());
            tourResponseDTO.setTourEnd(item.getTourEnd());
            tourResponseDTO.setTourPrice(item.getPriceAdult());
            tourResponseDTOS.add(tourResponseDTO);
        }

        return new PageImpl<>(tourResponseDTOS, pageable, tourEntities.getTotalElements());
    }

    @Override
    public TourRequestDTO getTourById(Long id) {
        TourEntity tourEntity = tourRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Tour Not Found"));
        TourRequestDTO tourRequestDTO = modelMapper.map(tourEntity, TourRequestDTO.class);
        System.out.println("ok");
        return tourRequestDTO;
    }


    @Override
    public TourRequestDTO update(long id, TourRequestDTO tourRequestDTO) {
        TourEntity tour = tourRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tour not found with ID: " + id));

        // Cập nhật các trường của tour từ tourRequestDTO, ngoại trừ farmTourEntities
        tour.setTourName(tourRequestDTO.getTourName());
        tour.setDecription(tourRequestDTO.getDecription());
        tour.setTourStart(tourRequestDTO.getTourStart());
        tour.setTourStart(tourRequestDTO.getTourStart());
        tour.setTourEnd(tourRequestDTO.getTourEnd());
        tour.setImage(tourRequestDTO.getImage());

        List<FarmTourRequestDTO> newFarmTourRequests = tourRequestDTO.getListFarmTour();
        List<FarmTourEntity> currentFarmTourEntities = tour.getFarmTourEntities();

        // Xóa các FarmTourEntity không còn trong danh sách mới
        currentFarmTourEntities.removeIf(existingFarmTour ->
                newFarmTourRequests.stream().noneMatch(newFarmTour ->
                        newFarmTour.getFarmId() == existingFarmTour.getFarm().getId()
                )
        );

        // Cập nhật hoặc thêm các FarmTourEntity mới từ newFarmTourRequests
        for (FarmTourRequestDTO item : newFarmTourRequests) {
            FarmEntity farm = farmRepository.findById(item.getFarmId())
                    .orElseThrow(() -> new EntityNotFoundException("Farm not found with ID: " + item.getFarmId()));

            FarmTourEntity farmTourEntity = currentFarmTourEntities.stream()
                    .filter(existingFarmTour -> existingFarmTour.getFarm().getId() == item.getFarmId())
                    .findFirst()
                    .orElse(new FarmTourEntity());

            farmTourEntity.setDescription(item.getDescription());
            farmTourEntity.setTour(tour);
            farmTourEntity.setFarm(farm);

            // Nếu là farm tour mới, thêm vào danh sách
            if (!currentFarmTourEntities.contains(farmTourEntity)) {
                currentFarmTourEntities.add(farmTourEntity);
            }
        }

        // Lưu thay đổi của tourEntity
        TourEntity updatedTour = tourRepository.save(tour);

        // Chuyển đổi lại sang DTO để trả về
        return modelMapper.map(updatedTour, TourRequestDTO.class);
    }


    @Override
    public boolean delete(long id) {
        Optional<TourEntity> tourEntity = tourRepository.findById(id);
        if (tourEntity.isPresent()) {
            TourEntity tour = tourEntity.get();
            tourRepository.delete(tour);
            return true;
        }else{
           return false;
        }
    }


}
