package com.BE.model.entity;

import com.BE.enums.BookingStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking")
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "tourId")
    private TourEntity tour;

    @Column(name = "numberOfAdult")
    private long numberOfAdult;

    @Column(name = "numberOfChild")
    private long numberOfChild;

    @Column(name = "totalPrice")
    private long totalPrice;

    @Column(name = "dayTime")
    private Date dateTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingStatusEnum status;

    @OneToOne(mappedBy = "booking")
    private OrderEntity order;

    @OneToMany(mappedBy = "booking",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ProcessingEntity>  processing;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<FeedbackEntity> feedbacks;

    @OneToMany(mappedBy = "booking")
    private List<BookingKoiFishEntity> bookingKoiFish;

}
