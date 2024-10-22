package com.BE.service.impl;

import com.BE.Converter.Converter;
import com.BE.model.entity.FarmEntity;
import com.BE.model.entity.FarmKoiEntity;
import com.BE.model.entity.KoiFishEntity;
import com.BE.model.request.FarmKoiRequestDTO;
import com.BE.model.request.KoiRequestDTO;
import com.BE.repository.FarmKoiRepository;
import com.BE.repository.FarmRepository;
import com.BE.repository.KoiFishRepository;
import com.BE.service.KoiFishService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class KoiFishServiceImpl implements KoiFishService {
    @Autowired
    KoiFishRepository koiFishRepository;
    @Autowired
    FarmRepository farmRepository;
    @Autowired
    FarmKoiRepository farmKoiRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private Converter converter;

    @Override
    public List<KoiRequestDTO> getAllKoiFish() {
        List<KoiFishEntity> listKoiFish = koiFishRepository.findAll();
        List<KoiRequestDTO> koiRequestDTOList = new ArrayList<>();

        for (KoiFishEntity entity : listKoiFish) {
            KoiRequestDTO koiRequestDTO = new KoiRequestDTO();
            koiRequestDTO.setId(entity.getId());
            koiRequestDTO.setKoiName(entity.getKoiName());
            koiRequestDTO.setPrice(entity.getPrice());
            koiRequestDTO.setDetail(entity.getDetail());
            koiRequestDTO.setImage(entity.getImage());

            // Ánh xạ thủ công FarmKoiEntity sang FarmKoiRequestDTO
            List<FarmKoiRequestDTO> farmKoiList = entity.getFarmKoisEntities().stream().map(farmKoiEntity -> {
                FarmKoiRequestDTO farmKoiRequestDTO = new FarmKoiRequestDTO();
                farmKoiRequestDTO.setKoiId(farmKoiEntity.getKoiFish().getId());
                farmKoiRequestDTO.setFarmId(farmKoiEntity.getFarmKoi().getId());
                farmKoiRequestDTO.setQuantity(farmKoiEntity.getQuantity());
                return farmKoiRequestDTO;
            }).collect(Collectors.toList());

            koiRequestDTO.setFarmKoiList(farmKoiList);
            koiRequestDTOList.add(koiRequestDTO);
        }

        return koiRequestDTOList;
    }

    @Override
    public KoiFishEntity add(KoiRequestDTO addKoiRequestDTO) {
        KoiFishEntity koiFishEntity = new KoiFishEntity();
        koiFishEntity.setKoiName(addKoiRequestDTO.getKoiName());
        koiFishEntity.setDetail(addKoiRequestDTO.getDetail());
        koiFishEntity.setPrice(addKoiRequestDTO.getPrice());
        koiFishEntity.setImage(addKoiRequestDTO.getImage());

        List<FarmKoiEntity> listFarmKoi = addKoiRequestDTO.getFarmKoiList().stream().map(item -> {
            FarmEntity farm = farmRepository.findById(item.getFarmId())
                    .orElseThrow(() -> new EntityNotFoundException("Farm not found with ID: " + item.getFarmId()));
            FarmKoiEntity farmKoiEntity = new FarmKoiEntity();
            farmKoiEntity.setQuantity(item.getQuantity());
            farmKoiEntity.setKoiFish(koiFishEntity);
            farmKoiEntity.setFarmKoi(farm);
            return farmKoiEntity;
        }).collect(Collectors.toList());

        koiFishEntity.setFarmKoisEntities(listFarmKoi);
        return koiFishRepository.save(koiFishEntity);
    }

    @Override
    public boolean update(long id, KoiRequestDTO koiRequestDTO) {
        KoiFishEntity koiFishEntity = koiFishRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Koi Fish not found with ID: " + id));

        // Map các trường khác ngoại trừ farmKoisEntities
        koiFishEntity.setKoiName(koiRequestDTO.getKoiName());
        koiFishEntity.setDetail(koiRequestDTO.getDetail());
        koiFishEntity.setPrice(koiRequestDTO.getPrice());
        koiFishEntity.setImage(koiRequestDTO.getImage());

        List<FarmKoiRequestDTO> listFarm = koiRequestDTO.getFarmKoiList();
        List<FarmKoiEntity> currentFarmKoisEntities = koiFishEntity.getFarmKoisEntities();

        // Xóa các farm koi không còn trong list mới
        currentFarmKoisEntities.removeIf(existingFarmKoi ->
                listFarm.stream().noneMatch(newFarmKoi ->
                        newFarmKoi.getFarmId() == existingFarmKoi.getFarmKoi().getId()
                )
        );

        // Cập nhật hoặc thêm farm koi mới
        for (FarmKoiRequestDTO item : listFarm) {
            FarmEntity farm = farmRepository.findById(item.getFarmId())
                    .orElseThrow(() -> new EntityNotFoundException("Farm not found with ID: " + item.getFarmId()));

            FarmKoiEntity farmKoiEntity = currentFarmKoisEntities.stream()
                    .filter(existingFarmKoi -> existingFarmKoi.getFarmKoi().getId() == item.getFarmId())
                    .findFirst()
                    .orElse(new FarmKoiEntity());

            farmKoiEntity.setQuantity(item.getQuantity());
            farmKoiEntity.setKoiFish(koiFishEntity);
            farmKoiEntity.setFarmKoi(farm);

            // Nếu là farm koi mới, thêm vào danh sách
            if (!currentFarmKoisEntities.contains(farmKoiEntity)) {
                currentFarmKoisEntities.add(farmKoiEntity);
            }
        }

        // Lưu lại koiFishEntity
        koiFishRepository.save(koiFishEntity);
        return true;
    }

    @Override
    public boolean delete(long id) {
        Optional<KoiFishEntity> koiFish = koiFishRepository.findById(id);
        if (koiFish.isPresent()) {
            KoiFishEntity koiFishEntity = koiFish.get();
            koiFishRepository.delete(koiFishEntity);
            return true;
        }
        return false;
    }

    @Override
    public KoiRequestDTO findById(long id) {
        KoiFishEntity koiFish = koiFishRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Koi Fish not found with ID: " + id));
        KoiRequestDTO newKoi = new KoiRequestDTO();
        newKoi.setId(koiFish.getId());
        newKoi.setKoiName(koiFish.getKoiName());
        newKoi.setDetail(koiFish.getDetail());
        newKoi.setPrice(koiFish.getPrice());
        newKoi.setImage(koiFish.getImage());
        newKoi.setDetail(koiFish.getDetail());
        newKoi.setFarmKoiList(converter.convertFarmKoiToEntity(koiFish.getFarmKoisEntities()));
        return newKoi;
    }

}
