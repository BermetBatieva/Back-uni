package com.example.Backuni.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "building")
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String description;

    private byte[] image;

    @Column(name = "link_2_gis")
    private String link2gis;

    @Column(name = "quantity_of_floor")
    private Integer quantityOfFloor;



}
