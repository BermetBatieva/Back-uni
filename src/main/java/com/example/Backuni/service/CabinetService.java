package com.example.Backuni.service;

import com.example.Backuni.dto.*;
import com.example.Backuni.entity.Building;
import com.example.Backuni.entity.Cabinet;
import com.example.Backuni.entity.Status;
import com.example.Backuni.exception.AlreadyExistException;
import com.example.Backuni.exception.ResourceNotFoundException;
import com.example.Backuni.repository.BuildingRepository;
import com.example.Backuni.repository.CabinetPaginationRepository;
import com.example.Backuni.repository.CabinetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CabinetService {

    @Autowired
    private CabinetRepository cabinetRepository;

    @Autowired
    private CabinetPaginationRepository cabinetPaginationRepository;

    @Autowired
    private BuildingRepository buildingRepository;



    public Cabinet add(CabinetDto cabinetDto) {
            Cabinet cabinet = new Cabinet();
            return saverCabinet(cabinet, cabinetDto);
    }

    @Transactional
    Cabinet saverCabinet(Cabinet cabinet, CabinetDto cabinetDto) {
        cabinet.setBuilding(buildingRepository.findById(cabinetDto.getBuildingId())
                .orElseThrow(()-> new ResourceNotFoundException("здание с таким id не найден")));
        cabinet.setStatus(Status.ACTIVATE);
        cabinet.setNumber(cabinetDto.getNumber());
        cabinet.setDescription(cabinetDto.getDescription());
        cabinet.setImage(cabinetDto.getImage());
        cabinet.setFloorNumber(cabinetDto.getFloorNumber());
        cabinet.setImage(cabinetDto.getImage());
        cabinetRepository.save(cabinet);
        return cabinet;
    }

    public List<ListCabinets> getAllCabinetsByBuildingIdAndFloorNum(CabinetsByBuildingIdAndFloorNum ids){
        List<Cabinet> cabinets = cabinetRepository.findByBuilding_IdAndFloorNumberAndStatus(ids.getBuildingId(),
                ids.getFloorNum(),Status.ACTIVATE);

        List<ListCabinets> result = new ArrayList<>();

        for(Cabinet cabinet : cabinets){
            ListCabinets model = new ListCabinets();
            model.setId(cabinet.getId());
            model.setImage(cabinet.getImage());
            model.setNumber(cabinet.getNumber());
            model.setDescription(cabinet.getDescription());
            result.add(model);
        }
        return result;
    }

    public CabinetDto getById(Long id){
        Cabinet cabinet = cabinetRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("нет кабинета с таким id = ",id));
        CabinetDto cabinetDto = new CabinetDto();
        cabinetDto.setName(cabinet.getName());
        cabinetDto.setFloorNumber(cabinet.getFloorNumber());
        cabinetDto.setImage(cabinet.getImage());
        cabinetDto.setDescription(cabinet.getDescription());
        cabinetDto.setNumber(cabinet.getNumber());
        return cabinetDto;
    }

    @Transactional
    public DeletedDTO deleteById(Long id) {
        Cabinet cabinet = cabinetRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("cabinet с таким id не существует! id = ", id));
        cabinet.setStatus(Status.DELETED);
        cabinetRepository.save(cabinet);
        return new DeletedDTO(id);
    }

    public Cabinet updateById(Long id,CabinetDto cabinetDto) {
        Cabinet cabinet = cabinetRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("cabinet с таким id не существует! id = ", id));
        cabinet.setStatus(Status.ACTIVATE);
        cabinet.setImage(cabinetDto.getImage());
        cabinet.setDescription(cabinetDto.getDescription());
        cabinet.setName(cabinetDto.getName());
        cabinet.setName(cabinetDto.getName());
        cabinet.setFloorNumber(cabinetDto.getFloorNumber());
        cabinet.setNumber(cabinetDto.getNumber());
        cabinetRepository.save(cabinet);

        return cabinet;
    }


    private CabinetDto convertToBuildingModel(Cabinet cabinet){
            CabinetDto cabinetDto = new CabinetDto();

            cabinetDto.setId(cabinet.getId());
            cabinetDto.setName(cabinet.getName());
            cabinetDto.setNumber(cabinet.getNumber());
            cabinetDto.setImage(cabinet.getImage());
            cabinetDto.setFloorNumber(cabinet.getFloorNumber());

            return cabinetDto;
        }

    public Page<CabinetDto> getAllCabinets(Long buildId,Integer floorNum,Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        List<Cabinet> buildingList = cabinetRepository.findByBuilding_IdAndFloorNumberAndStatus(buildId,floorNum,Status.ACTIVATE);
        Page<Cabinet> buildingPage = cabinetPaginationRepository.findAll(pageable);
        List<CabinetDto> transactionModelList = new ArrayList<>();

        buildingPage.forEach(building -> {
            CabinetDto model = convertToBuildingModel(building);

            transactionModelList.add(model);
        });

        return new PageImpl<>(transactionModelList, PageRequest.of(pageNo, pageSize, Sort.by(sortBy)), buildingList.size());
    }
}
