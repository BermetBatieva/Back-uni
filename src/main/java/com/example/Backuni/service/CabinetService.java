package com.example.Backuni.service;

import com.example.Backuni.dto.BuildingDto;
import com.example.Backuni.dto.CabinetDto;
import com.example.Backuni.dto.CabinetsByBuildingIdAndFloorNum;
import com.example.Backuni.dto.ListCabinets;
import com.example.Backuni.entity.Building;
import com.example.Backuni.entity.Cabinet;
import com.example.Backuni.exception.AlreadyExistException;
import com.example.Backuni.exception.ResourceNotFoundException;
import com.example.Backuni.repository.BuildingRepository;
import com.example.Backuni.repository.CabinetRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private BuildingRepository buildingRepository;



    public CabinetDto add(CabinetDto cabinetDto) throws AlreadyExistException {
        if (cabinetRepository.existsByNumber(cabinetDto.getNumber())) {
            throw new AlreadyExistException("уже есть кабинет с таким номером!");
        } else {
            Cabinet cabinet = new Cabinet();
            return saverCabinet(cabinet, cabinetDto);
        }
    }

    @Transactional
    CabinetDto saverCabinet(Cabinet cabinet, CabinetDto cabinetDto) {
        cabinet.setBuilding(buildingRepository.findById(cabinetDto.getBuildingId())
                .orElseThrow(()-> new ResourceNotFoundException("здание с таким id не найден")));
        cabinet.setNumber(cabinetDto.getNumber());
        cabinet.setDescription(cabinetDto.getDescription());
        cabinet.setImage(cabinetDto.getImage());
        cabinet.setFloorNumber(cabinetDto.getFloorNumber());
        cabinet.setImage(cabinetDto.getImage());
        cabinetRepository.save(cabinet);
        return cabinetDto;
    }

    public List<ListCabinets> getAllCabinetsByBuildingIdAndFloorNum(CabinetsByBuildingIdAndFloorNum ids){
        List<Cabinet> cabinets = cabinetRepository.findByBuilding_IdAndFloorNumber(ids.getBuildingId(),
                ids.getFloorNum());

        List<ListCabinets> result = new ArrayList<>();

        for(Cabinet cabinet : cabinets){
            ListCabinets model = new ListCabinets();
            model.setNumber(cabinet.getNumber());
            model.setDescription(cabinet.getDescription());
            result.add(model);
        }
        return result;
    }

    public CabinetDto getById(Long id){
        Optional<Cabinet> cabinet = cabinetRepository.findById(id);
        CabinetDto cabinetDto = new CabinetDto();
        cabinetDto.setName(cabinet.get().getName());
        cabinetDto.setFloorNumber(cabinet.get().getFloorNumber());
        cabinetDto.setImage(cabinet.get().getImage());
        cabinetDto.setDescription(cabinet.get().getDescription());
        cabinetDto.setNumber(cabinet.get().getNumber());
        return cabinetDto;
    }


}
