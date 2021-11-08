package com.example.Backuni.service;

import com.example.Backuni.dto.BuildingDto;
import com.example.Backuni.entity.Building;
import com.example.Backuni.exception.AlreadyExistException;
import com.example.Backuni.exception.ResourceNotFoundException;
import com.example.Backuni.repository.BuildingPaginationRepository;
import com.example.Backuni.repository.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.*;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class BuildingService {

    @Autowired
    private BuildingRepository repository;

    @Autowired
    private BuildingPaginationRepository buildingPaginationRepository;


    public BuildingDto addBuilding(BuildingDto dto) {
            Building building = new Building();
            building.setAddress(dto.getAddress());
            building.setDescription(dto.getDescription());
            building.setName(dto.getName());
            building.setImage(dto.getImage());
            building.setQuantityOfFloor(dto.getQuantityOfFloor());
            building.setLink2gis(dto.getLink2gis());
            repository.save(building);
            return dto;
    }

    public Page<BuildingDto> getAllBuildings(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        List<Building> buildingList = repository.findAll();
        Page<Building> buildingPage = buildingPaginationRepository.findAll(pageable);
        List<BuildingDto> transactionModelList = new ArrayList<>();

        buildingPage.forEach(building -> {
            BuildingDto model = convertToBuildingModel(building);

            transactionModelList.add(model);
        });

        return new PageImpl<>(transactionModelList, PageRequest.of(pageNo, pageSize, Sort.by(sortBy)), buildingList.size());
    }

    private BuildingDto convertToBuildingModel(Building building){
        BuildingDto buildingDto = new BuildingDto();

        buildingDto.setId(building.getId());
        buildingDto.setName(building.getName());
        buildingDto.setAddress(building.getAddress());
        buildingDto.setImage(building.getImage());
        buildingDto.setLink2gis(building.getLink2gis());
        buildingDto.setQuantityOfFloor(building.getQuantityOfFloor());

        return buildingDto;
    }

    public List<BuildingDto> allBuilding() {
        List<Building> list = repository.findAll();
        List<BuildingDto> result = new ArrayList<>();
        for (Building building : list) {
            BuildingDto model = new BuildingDto();
            model.setImage(building.getImage());
            model.setName(building.getName());
            model.setAddress(building.getAddress());
            model.setQuantityOfFloor(building.getQuantityOfFloor());
            model.setLink2gis(building.getLink2gis());
            result.add(model);
        }
        return result;
    }

    public BuildingDto getById(Long id){
        Optional<Building> building = repository.findById(id);
        BuildingDto buildingDto = new BuildingDto();
        buildingDto.setId(building.get().getId());
        buildingDto.setName(building.get().getName());
        buildingDto.setLink2gis(building.get().getLink2gis());
        buildingDto.setAddress(building.get().getAddress());
        buildingDto.setImage(building.get().getImage());
        return buildingDto;
    }

    public List<Integer> getAllFloor(Long id){
        Optional<Building> building = repository.findById(id);
        List<Integer> floors = new ArrayList<>();

        for(int i = 1; i <= building.get().getQuantityOfFloor();i++){
            floors.add(i);
        }
        return floors;
    }
}