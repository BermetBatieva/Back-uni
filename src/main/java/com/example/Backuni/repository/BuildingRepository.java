package com.example.Backuni.repository;

import com.example.Backuni.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
    boolean existsBuildingByName(String name);





}
