package com.example.Backuni.dto;

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


}