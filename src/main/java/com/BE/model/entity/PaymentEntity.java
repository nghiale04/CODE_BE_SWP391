package com.BE.model.entity;

import com.BE.enums.PaymentEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class PaymentEntity {
    @Id
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    private long id;

    @Column(name = "date")
    private Date date;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentEnum paymentMethod;

    @OneToOne
    @JoinColumn(name = "order_id")
    OrderEntity order;


}
