package com.BE.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.query.Order;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "koifish")
public class KoiFishEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(name = "KoiName", nullable = false)
    private String koiName;

    @Column(name = "Detail", nullable = false)
    private String detail;

    @Column(name = "Price", nullable = false)
    private Long price;

    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "koiFish", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<FarmKoiEntity> farmKoisEntities = new ArrayList<>();

}
