package com.example.Backuni.dto;

import com.example.Backuni.entity.BuildingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BuildingDto {

    private Long id;

    private String name;

    private String address;

    private String link2gis;

    private String description;

    private byte[] image;

    private Integer quantityOfFloor;

    private String totalArea;

    private String usableArea;

    private Long yearOfConstruction;

    private BuildingType buildingType;


}
