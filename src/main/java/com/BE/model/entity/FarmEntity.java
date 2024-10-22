package com.BE.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table (name = "farms")
public class FarmEntity implements Serializable {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column (name = "FarmName", nullable = false)
    private String farmName;

    @Column (name = "location", nullable = false)
    private String location;

    @Column (name = "Description", nullable = false)
    private String description;

    @Column (name = "StartTime" )
    private Time startTime;

    @Column (name = "EndTime")
    private Time endTime;

    @Column (name = "image")
    private String image;

    @OneToMany(mappedBy = "farm", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<FarmTourEntity> farmTourEntities = new ArrayList<>();

    @OneToMany(mappedBy = "farmKoi", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<FarmKoiEntity> farmKoisEntities = new ArrayList<>();
}
