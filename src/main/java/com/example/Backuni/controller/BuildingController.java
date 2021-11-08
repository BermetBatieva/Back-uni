package com.example.Backuni.controller;

import com.example.Backuni.dto.AuthenticationResponse;
import com.example.Backuni.dto.BuildingDto;
import com.example.Backuni.entity.Building;
import com.example.Backuni.exception.AlreadyExistException;
import com.example.Backuni.repository.BuildingRepository;
import com.example.Backuni.service.BuildingService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.net.http.HttpResponse;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("building")
public class BuildingController {

    @Autowired
    private BuildingService service;

    @Autowired
    private BuildingRepository repository;


    @ApiOperation("получение всех зданий с пагинацией")
    @GetMapping("/get-all-building-pagination")
    public Page<BuildingDto> getAllBuildingForWeb(@RequestParam(defaultValue = "1") Integer pageNo,
                                            @RequestParam(defaultValue = "15") Integer pageSize,
                                            @RequestParam(defaultValue = "id") String sortBy){
        return service.getAllBuildings(pageNo,pageSize,sortBy);
    }

    @PostMapping("add")
    public ResponseEntity<BuildingDto> addNewBuilding(@RequestBody BuildingDto model) {
        if(!repository.existsBuildingByName(model.getName())) {
            service.addBuilding(model);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @ApiOperation("получение всех зданий")
    @GetMapping("get-all-building")
    public List<BuildingDto> getAllBuilding(){
        return service.allBuilding();
    }


    @GetMapping("get-by-id/{id}")
    public ResponseEntity<BuildingDto> getByBuildId(@PathVariable Long id){
        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
    }


    @ApiOperation("получение всех этажей")
    @GetMapping("get-all-floor-by-build/{id}")
    public List<Integer> getAllFloorsByBuild(@PathVariable Long id){
       return service.getAllFloor(id);
    }
}
