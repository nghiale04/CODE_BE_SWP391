package com.BE.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table (name = "tours")
public class TourEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column (name = "TourName", nullable = false)
    private String tourName;

    @Column (name = "TourStart", nullable = false)
    private Date tourStart;

    @Column (name = "TourEnd", nullable = false)
    private Date tourEnd;

    @Column (name="Decription")
    private String decription;

    @Column (name = "PriceAdult", nullable = false)
    private Long priceAdult;

    @Column (name = "PriceChild", nullable = false)
    private Long priceChild;

    @Column (name = "Recipients", nullable = false)
    private Long recipients;

    @Column (name = "image")
    private String image;

    @OneToMany (mappedBy = "tour", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FarmTourEntity> farmTourEntities = new ArrayList<>();

    @OneToMany(mappedBy = "tour")
    @JsonIgnore
    private List<BookingEntity> bookingList = new ArrayList<>();
}
