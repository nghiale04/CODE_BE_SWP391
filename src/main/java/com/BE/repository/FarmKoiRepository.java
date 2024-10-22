package com.BE.repository;

import com.BE.model.entity.FarmEntity;
import com.BE.model.entity.FarmKoiEntity;
import com.BE.model.entity.KoiFishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FarmKoiRepository extends JpaRepository<FarmKoiEntity, Long> {
    Optional<FarmKoiEntity> findById(Long id);
    List<FarmKoiEntity> findAllByFarmKoi(FarmEntity farmKoi);
    FarmKoiEntity findByFarmKoiAndKoiFish(FarmEntity farmKoi, KoiFishEntity koiFish);
}
