package com.BE.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking_koifish")
public class BookingKoiFishEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "quantity")
    private long quantity;

    @Column(name = "pricePerUnit")
    private long pricePerUnit;

    @Column(name ="totalPrice")
    private long totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="booking_id")
    private BookingEntity booking;

    @ManyToOne
    @JoinColumn(name = "farmkoi_id")
    private FarmKoiEntity farmKoiEntity;

}
