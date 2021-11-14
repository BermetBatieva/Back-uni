package com.example.Backuni.controller;

import com.example.Backuni.dto.*;
import com.example.Backuni.entity.Building;
import com.example.Backuni.entity.Cabinet;
import com.example.Backuni.exception.AlreadyExistException;
import com.example.Backuni.service.CabinetService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("cabinet")
public class CabinetController {

    @Autowired
    private CabinetService cabinetService;

    @ApiOperation(value = "Добавление кабинета")
    @PostMapping("/add")
    public ResponseEntity<Cabinet> create(@RequestBody CabinetDto cabinetDto) {
        return new ResponseEntity<>(cabinetService.add(cabinetDto), HttpStatus.OK);
    }
    @ApiOperation(value = "получение всех кабинетов по зданию и этажу")
    @GetMapping("get-all-by-build-and-floor")
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
}
