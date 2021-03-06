package com.example.Backuni.controller;

import com.example.Backuni.dto.*;
import com.example.Backuni.entity.Building;
import com.example.Backuni.entity.Cabinet;
import com.example.Backuni.exception.AlreadyExistException;
import com.example.Backuni.repository.CabinetRepository;
import com.example.Backuni.service.CabinetService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("cabinet")
public class CabinetController {

    @Autowired
    private CabinetService cabinetService;

    @Autowired
    private CabinetRepository repository;



    @PutMapping("/image/{cabinetId}")
    public ResponseEntity<Cabinet> setImage(@RequestParam(name = "file") MultipartFile[] multipartFile,
                                             @PathVariable Long cabinetId, @RequestParam(name = "text")Long idBuild) throws  IOException {
        return cabinetService.setImage(multipartFile,cabinetId,idBuild);
    }

    @ApiOperation(value = "Добавление кабинета")
    @PostMapping("/add")
    public ResponseEntity<Cabinet> create(@RequestBody CabinetDto cabinetDto) {
        if(!repository.existsByNumberAndBuilding_id(cabinetDto.getNumber(),cabinetDto.getBuildingId())) {
            return new ResponseEntity<>(cabinetService.add(cabinetDto), HttpStatus.OK);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @ApiOperation(value = "получение всех кабинетов по зданию и этажу")
    @PostMapping("get-all-by-build-and-floor")
    public List<ListCabinets> getAllByBuildAndFloor(@RequestBody CabinetsByBuildingIdAndFloorNum c){
        return cabinetService.getAllCabinetsByBuildingIdAndFloorNum(c);
    }

    @GetMapping("get-by-id/{id}")
    public ResponseEntity<CabinetDto> getByCabinetId(@PathVariable Long id){
        return new ResponseEntity<>(cabinetService.getById(id), HttpStatus.OK);
    }

    @ApiOperation("Удаление кабинета")
    @DeleteMapping("delete/{id}")
    public DeletedDTO deleteMenuById(@PathVariable Long id) {
        return cabinetService.deleteById(id);
    }

    @ApiOperation("редактирование кабинета")
    @PutMapping("edit/{id}")
    public ResponseEntity<Cabinet> update(@PathVariable Long id, @RequestBody CabinetDto model) {
        return new ResponseEntity<>(cabinetService.updateById(id,model),HttpStatus.OK);
    }

    @ApiOperation("получение всех кабинетов по зданию и этажу с пагинацией")
    @PostMapping("/get-all-cabinet-pagination")
    public Page<CabinetDto> getAllCabinetForWeb(@RequestBody CabinetsByBuildingIdAndFloorNum c,
                                                @RequestParam(defaultValue = "1") Integer pageNo,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(defaultValue = "id") String sortBy){
        return cabinetService.getAllCabinets(c.getBuildingId(),c.getFloorNum(),pageNo,pageSize,sortBy);
    }

}
