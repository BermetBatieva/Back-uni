package com.example.Backuni.controller;

import com.example.Backuni.dto.BuildingDto;
import com.example.Backuni.dto.DeletedDTO;
import com.example.Backuni.entity.Building;
import com.example.Backuni.repository.BuildingRepository;
import com.example.Backuni.service.BuildingService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                            @RequestParam(defaultValue = "id") String sortBy){
        return service.getAllBuildings(pageNo,pageSize,sortBy);
    }

    @PostMapping("add")
    public ResponseEntity<Building> addNewBuilding(@RequestBody BuildingDto model) {
        if(!repository.existsBuildingByName(model.getName())) {
            return new ResponseEntity<>( service.addBuilding(model), HttpStatus.OK);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @ApiOperation("получение всех зданий")
    @GetMapping("get-all-building")
    public List<Building> getAllBuilding(){
        return service.allBuilding();
    }

    @ApiOperation("получение всех зданий по категории пагинация")
    @GetMapping("get-all-building-by-category-and-type/{categoryId}")
    public List<BuildingDto> getAllBuildingByCategoryId(@PathVariable Long categoryId,
                                                     @RequestParam(defaultValue = "1") Integer pageNo,
                                                     @RequestParam(defaultValue = "10") Integer pageSize,
                                                     @RequestParam(defaultValue = "id") String sortBy){
        return service.getAllBuildingsByCategory(categoryId,pageNo,pageSize,sortBy);
    }

    @GetMapping("get-by-id/{id}")
    public ResponseEntity<BuildingDto> getByBuildId(@PathVariable Long id){
        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
    }


    @ApiOperation("получение всех этажей по зданию")
    @GetMapping("get-all-floor-by-build/{id}")
    public List<Integer> getAllFloorsByBuild(@PathVariable Long id){
       return service.getAllFloor(id);
    }

    @ApiOperation("Удаление здания")
    @DeleteMapping("delete/{id}")
    public DeletedDTO deleteMenuById(@PathVariable Long id) {
        return service.deleteById(id);
    }

    @ApiOperation("редактирование здания")
    @PutMapping("edit/{id}")
    public ResponseEntity<Building> update(@PathVariable Long id,@RequestBody BuildingDto model) {
        return new ResponseEntity<>(service.updateById(id,model),HttpStatus.OK);
    }
}
