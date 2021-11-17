package com.example.Backuni.dto;

import com.example.Backuni.entity.BuildingType;
import com.example.Backuni.entity.LinkToMap;
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

    private LinkToMapDto link;

    private String description;

    private String imageLink;

    private Integer quantityOfFloor;

    private String totalArea;

    private String usableArea;

    private Long yearOfConstruction;

    private BuildingType buildingType;

    private Long categoryId;


}
