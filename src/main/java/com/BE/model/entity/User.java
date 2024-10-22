package com.BE.model.entity;


import com.BE.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.*;


@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(unique = true)
    String fullName;

    @Column(unique = true)
    String address;

    @Column(unique = true)
    String phone;

    float balance = 0;

    @Column(unique = true)
    String email;

    @Column(unique = true)
    String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

    @Enumerated(value = EnumType.STRING)
    RoleEnum role;

    @OneToMany(mappedBy = "user")
    private List<BookingEntity> bookingList = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<OrderEntity> orderList = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<FeedbackEntity> feedbackList;

}
