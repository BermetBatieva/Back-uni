package com.example.Backuni.dto;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CabinetDto {

    private Long buildingId;

    private Long id;

    private Long number;

    private List<String> urlImage;

    private String name;

    private String description;

    private Integer floorNumber;
}
