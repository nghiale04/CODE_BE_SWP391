package com.BE.repository;

import com.BE.model.entity.FarmEntity;
import com.BE.model.entity.FarmTourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FarmTourRepository extends JpaRepository<FarmTourEntity, Long> {
    List<FarmTourEntity> findAllByFarm(FarmEntity farmEntity);
}
