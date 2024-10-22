package com.BE.repository;

import com.BE.model.entity.BookingKoiFishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingKoifishRepository extends JpaRepository<BookingKoiFishEntity, Long> {
}
