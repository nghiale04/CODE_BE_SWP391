package com.BE.repository;

import com.BE.model.entity.BookingEntity;
import com.BE.model.entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity,Long> {
    List<FeedbackEntity> findAllByBooking(BookingEntity booking);

}
