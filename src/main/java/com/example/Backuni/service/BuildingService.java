package com.example.Backuni.service;

import com.example.Backuni.dto.BuildingDto;
import com.example.Backuni.dto.DeletedDTO;
import com.example.Backuni.dto.LinkToMapDto;
import com.example.Backuni.entity.*;
import com.example.Backuni.exception.AlreadyExistException;
import com.example.Backuni.exception.ResourceNotFoundException;
import com.example.Backuni.repository.BuildingPaginationRepository;
import com.example.Backuni.repository.BuildingRepository;
import com.example.Backuni.repository.CategoryRepository;
import com.example.Backuni.repository.LinkToMapRepository;
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

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LinkToMapRepository linkToMapRepository;




    public Building addBuilding(BuildingDto dto) throws AlreadyExistException {
        if (repository.existsBuildingByNameAndStatus(dto.getName(), Status.ACTIVATE)) {
            throw new AlreadyExistException("здание с таким именем уже существует!");
        } else {
            Building building = new Building();
            return saver(building, dto);
        }
    }


    @Transactional
    Building saver(Building building, BuildingDto buildingDto) {
        building.setName(buildingDto.getName());
        building.setCategory(categoryRepository.findById(buildingDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("категория с такой id не найдено! id = ",
                        buildingDto.getCategoryId())));
        building.setDescription(buildingDto.getDescription());
        building.setImage(buildingDto.getImage());
        building.setTotalArea(buildingDto.getTotalArea());
        building.setUsableArea(buildingDto.getUsableArea());
        building.setStatus(Status.ACTIVATE);
        building.setType(buildingDto.getBuildingType());
        building.setQuantityOfFloor(buildingDto.getQuantityOfFloor());
        building.setYearOfConstruction(buildingDto.getYearOfConstruction());
        building.setAddress(buildingDto.getAddress());
        repository.save(building);
        Building buildId = repository.getByName(building.getName()).orElseThrow();
        long id = buildId.getId();

        //добавление link
        LinkToMapDto link = buildingDto.getLink();

        addAddressLink(link,id);
        return building;
    }

    private  void updateAddressLink(LinkToMapDto linkToMapDto, Long id){

        LinkToMap linkToMap = linkToMapRepository.findByBuilding_Id(id);

        linkToMap.setLat(linkToMapDto.getLat());
        linkToMap.setLon(linkToMap.getLon());
        linkToMap.setBuilding(repository.getById(id));

        linkToMapRepository.save(linkToMap);

    }

    private  void addAddressLink(LinkToMapDto linkToMapDto, Long id){

        LinkToMap linkToMap = new LinkToMap();

        linkToMap.setLat(linkToMapDto.getLat());
        linkToMap.setLon(linkToMap.getLon());
        linkToMap.setBuilding(repository.getById(id));

        linkToMapRepository.save(linkToMap);

    }

    @Transactional
    Building updater(Building building, BuildingDto buildingDto) {
        building.setName(buildingDto.getName());
        building.setCategory(categoryRepository.findById(buildingDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("категория с такой id не найдено! id = ",
                        buildingDto.getCategoryId())));
        building.setDescription(buildingDto.getDescription());
        building.setImage(buildingDto.getImage());
        building.setTotalArea(buildingDto.getTotalArea());
        building.setUsableArea(buildingDto.getUsableArea());
        building.setStatus(Status.ACTIVATE);
        building.setType(buildingDto.getBuildingType());
        building.setQuantityOfFloor(buildingDto.getQuantityOfFloor());
        building.setYearOfConstruction(buildingDto.getYearOfConstruction());
        building.setAddress(buildingDto.getAddress());
        repository.save(building);
        Building buildId = repository.getByName(building.getName()).orElseThrow();
        long id = buildId.getId();

        //добавление link
        LinkToMapDto link = buildingDto.getLink();

        updateAddressLink(link,id);
        return building;
    }

    private BuildingDto convertToBuildingModel(Building building){
        BuildingDto buildingDto = new BuildingDto();

        buildingDto.setId(building.getId());
        buildingDto.setName(building.getName());
        buildingDto.setBuildingType(building.getType());
        buildingDto.setQuantityOfFloor(building.getQuantityOfFloor());
        buildingDto.setCategoryId(building.getCategory().getId());
        buildingDto.setAddress(building.getAddress());
        buildingDto.setTotalArea(building.getTotalArea());
        buildingDto.setUsableArea(building.getUsableArea());
        buildingDto.setYearOfConstruction(building.getYearOfConstruction());
        buildingDto.setImage(building.getImage());


        buildingDto.setQuantityOfFloor(building.getQuantityOfFloor());

        return buildingDto;
    }



    public Page<BuildingDto> getAllBuildings(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        List<Building> buildingList = repository.findAllByStatus(Status.ACTIVATE);
        Page<Building> buildingPage = buildingPaginationRepository.findAll(pageable);
        List<BuildingDto> transactionModelList = new ArrayList<>();

        buildingPage.forEach(building -> {
            BuildingDto model = convertToBuildingModel(building);

            transactionModelList.add(model);
        });

        return new PageImpl<>(transactionModelList, PageRequest.of(pageNo, pageSize, Sort.by(sortBy)), buildingList.size());
    }

    public List<Building> allBuilding() {
        List<Building> list = repository.findAllByStatus(Status.ACTIVATE);
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



    public Building updateById(Long id,BuildingDto dto) {
        Building building = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("здание с таким id не существует! id = ", id));
            return updater(building, dto);
    }

    public List<BuildingDto> getAllBuildingsByCategory(long id,Integer pageNo, Integer pageSize, String sortBy) {
        List<Building> buildingListFilteredByCategory = repository.findByStatusAndCategory_Id(Status.ACTIVATE,id);
        List<BuildingDto> buildingModelsList = new ArrayList<>();

        buildingListFilteredByCategory.forEach(building -> {
            BuildingDto buildingDto = convertToBuildingModel(building);

            buildingModelsList.add(buildingDto);
        });

        return buildingModelsList;
    }

}
