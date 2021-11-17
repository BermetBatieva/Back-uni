package com.example.Backuni.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "building")
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name; //наименование организации

    private String address;

    private String description;

    private byte[] image;

    @Column(name = "quantity_of_floor")
    private Integer quantityOfFloor;//5

    @Column(name = "total_area")
    private String totalArea; //общая площадь


    @Column(name = "usable_area")
    private String usableArea;//полезная площадь

    @Column(name = "year_of_construction")
    private Long yearOfConstruction;//год постройки

    @ManyToOne
    private  Category category;


    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private BuildingType type;
}
