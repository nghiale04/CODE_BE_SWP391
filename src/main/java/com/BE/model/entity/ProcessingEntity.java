package com.BE.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "processing")
public class ProcessingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name= "type")
    private String type;

    @Column(name= "status")
    private int status;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private BookingEntity booking;

}
