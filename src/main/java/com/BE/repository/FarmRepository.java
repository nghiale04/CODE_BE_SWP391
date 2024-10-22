package com.BE.repository;

import com.BE.model.entity.FarmEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FarmRepository extends JpaRepository<FarmEntity, Long> {
    Optional<FarmEntity> findById(int id);
}
