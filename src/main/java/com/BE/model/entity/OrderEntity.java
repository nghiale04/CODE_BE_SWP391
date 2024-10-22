package com.BE.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="date")
    private Date date;

    @Column(name="total")
    private float total;

    @ManyToOne
    @JoinColumn(name = "customerId")
    @JsonIgnore
    private User customer;

//    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
//    List<OrderDetailEntity> orderDetails;

    @OneToOne(mappedBy = "order")
    @JsonIgnore
    PaymentEntity payment;

    @OneToOne
    @JoinColumn(name="order_id")
    @JsonIgnore
    private BookingEntity booking;
}
