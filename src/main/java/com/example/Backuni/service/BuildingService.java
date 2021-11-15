package com.example.Backuni.service;

import com.example.Backuni.dto.BuildingDto;
import com.example.Backuni.dto.DeletedDTO;
import com.example.Backuni.entity.Building;
import com.example.Backuni.entity.BuildingType;
import com.example.Backuni.entity.Category;
import com.example.Backuni.entity.Status;
import com.example.Backuni.exception.AlreadyExistException;
import com.example.Backuni.exception.ResourceNotFoundException;
import com.example.Backuni.repository.BuildingPaginationRepository;
import com.example.Backuni.repository.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class BuildingService {

    @Autowired
    private BuildingRepository repository;

    @Autowired
    private BuildingPaginationRepository buildingPaginationRepository;


    public Building addBuilding(BuildingDto dto) {
            Building building = new Building();
            building.setAddress(dto.getAddress());
            building.setTotalArea(dto.getTotalArea());
            building.setUsableArea(dto.getUsableArea());
            building.setYearOfConstruction(dto.getYearOfConstruction());
            building.setDescription(dto.getDescription());
            building.setName(dto.getName());
            building.setType(dto.getBuildingType());
            building.setImage(dto.getImage());
            building.setStatus(Status.ACTIVATE);
            building.setQuantityOfFloor(dto.getQuantityOfFloor());
            building.setLink2gis(dto.getLink2gis());
            repository.save(building);
            return building;
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
        buildingDto.setTotalArea(building.getTotalArea());
        buildingDto.setUsableArea(building.getUsableArea());
        buildingDto.setYearOfConstruction(building.getYearOfConstruction());
        buildingDto.setImage(building.getImage());
        buildingDto.setLink2gis(building.getLink2gis());
        buildingDto.setQuantityOfFloor(building.getQuantityOfFloor());

        return buildingDto;
    }

    public List<Building> allBuilding() {
        List<Building> list = repository.getBuildingByStatus(Status.ACTIVATE);
        List<BuildingDto> result = new ArrayList<>();
        for (Building building : list) {
            BuildingDto model = new BuildingDto();
            model.setImage(building.getImage());
            model.setName(building.getName());
            model.setAddress(building.getAddress());
            model.setYearOfConstruction(building.getYearOfConstruction());
            model.setTotalArea(building.getTotalArea());
            model.setUsableArea(building.getUsableArea());
            model.setDescription(building.getDescription());
            model.setQuantityOfFloor(building.getQuantityOfFloor());
            model.setLink2gis(building.getLink2gis());
            result.add(model);
        }
        return list;
    }

    public BuildingDto getById(Long id){
        Building building = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("здание с таким id не существует! id = ", id));
        BuildingDto buildingDto = new BuildingDto();
        buildingDto.setId(building.getId());
        buildingDto.setBuildingType(building.getType());
        buildingDto.setName(building.getName());
        buildingDto.setUsableArea(building.getUsableArea());
        buildingDto.setTotalArea(building.getTotalArea());
        buildingDto.setYearOfConstruction(building.getYearOfConstruction());
        buildingDto.setLink2gis(building.getLink2gis());
        buildingDto.setDescription(building.getDescription());
        buildingDto.setQuantityOfFloor(building.getQuantityOfFloor());
        buildingDto.setAddress(building.getAddress());
        buildingDto.setImage(building.getImage());
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

    @Transactional
    public DeletedDTO deleteById(Long id) {
        Building building = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("здание с таким id не существует! id = ", id));
        building.setStatus(Status.DELETED);
        repository.save(building);
        return new DeletedDTO(id);
    }

    public Building updateById(Long id,BuildingDto building){
        Building build = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("здание с таким id не существует! id = ", id));
        build.setStatus(Status.ACTIVATE);
        build.setImage(building.getImage());
        build.setDescription(building.getDescription());
        build.setName(building.getName());
        build.setYearOfConstruction(build.getYearOfConstruction());
        build.setTotalArea(building.getTotalArea());
        build.setUsableArea(building.getUsableArea());
        build.setAddress(building.getAddress());
        build.setLink2gis(building.getLink2gis());
        build.setQuantityOfFloor(building.getQuantityOfFloor());
        repository.save(build);

        return build;
    }




    public List<BuildingDto> getAllBuildingsByCategory(long id) {
        List<Building> buildingListFilteredByCategory = repository.findByCategory_Id(id);
        List<BuildingDto> buildingModelsList = new ArrayList<>();

        buildingListFilteredByCategory.forEach(building -> {
            BuildingDto buildingDto = convertToBuildingModel(building);

            buildingModelsList.add(buildingDto);
        });

        return buildingModelsList;
    }

}
