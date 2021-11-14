package com.example.Backuni.service;

import com.example.Backuni.dto.*;
import com.example.Backuni.entity.Cabinet;
import com.example.Backuni.entity.Status;
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



    public Cabinet add(CabinetDto cabinetDto) throws AlreadyExistException {
        if (cabinetRepository.existsByNumber(cabinetDto.getNumber())) {
            throw new AlreadyExistException("уже есть кабинет с таким номером!");
        } else {
            Cabinet cabinet = new Cabinet();
            return saverCabinet(cabinet, cabinetDto);
        }
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

    @Transactional
    public DeletedDTO deleteById(Long id) {
        Cabinet cabinet = cabinetRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("cabinet с таким id не существует! id = ", id));
        cabinet.setStatus(Status.DELETED);
        cabinetRepository.save(cabinet);
        return new DeletedDTO(id);
    }

    public Cabinet updateById(Long id,CabinetDto cabinetDto){
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


}
