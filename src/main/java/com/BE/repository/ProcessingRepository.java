package com.BE.repository;

import com.BE.model.entity.ProcessingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessingRepository extends JpaRepository<ProcessingEntity, Long> {
    List<ProcessingEntity> findAllByBookingId(Long bookingId);
}
