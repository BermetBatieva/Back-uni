package com.example.Backuni.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CabinetsByBuildingIdAndFloorNum {

    private Long buildingId;

    private Integer floorNum;
}
