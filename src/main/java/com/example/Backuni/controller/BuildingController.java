package com.example.Backuni.controller;

import com.example.Backuni.dto.BuildingDto;
import com.example.Backuni.dto.DeletedDTO;
import com.example.Backuni.entity.Building;
import com.example.Backuni.entity.Image;
import com.example.Backuni.exception.AlreadyExistException;
import com.example.Backuni.repository.BuildingRepository;
import com.example.Backuni.service.BuildingService;
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
@RequestMapping("building")
public class BuildingController {

    @Autowired
    private BuildingService service;

    @Autowired
    private BuildingRepository repository;




    @PutMapping("/image/{buildId}")
    public ResponseEntity<Building> setImage(@RequestParam(name = "file") MultipartFile[] multipartFile, //больше одного RequestParam нельзя, когда MultipartFile
                                            @PathVariable Long buildId) throws IOException, IOException {
        return service.setImage(multipartFile,buildId);
    }

    @ApiOperation("получение всех зданий с пагинацией")
    @GetMapping("/get-all-building-pagination")
    public Page<BuildingDto> getAllBuildingForWeb(@RequestParam(defaultValue = "1") Integer pageNo,
                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                            @RequestParam(defaultValue = "id") String sortBy){
        return service.getAllBuildings(pageNo,pageSize,sortBy);
    }

    @PostMapping("add")
    public ResponseEntity<Building> addNewBuilding(@RequestBody BuildingDto model) throws AlreadyExistException {
            return new ResponseEntity<>( service.addBuilding(model), HttpStatus.OK);
    }


    @ApiOperation("получение всех зданий")
    @GetMapping("get-all-building")
    public List<BuildingDto> getAllBuilding(){
        return service.allBuilding();
    }

    @ApiOperation("получение всех зданий по категории пагинация")
    @GetMapping("get-all-building-by-category-and-type/{categoryId}")
    public List<BuildingDto> getAllBuildingByCategoryId(@PathVariable Long categoryId){
        return service.getByCategoryId(categoryId);
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
