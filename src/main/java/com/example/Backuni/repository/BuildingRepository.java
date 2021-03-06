package com.example.Backuni.repository;

import com.example.Backuni.entity.Building;
import com.example.Backuni.entity.BuildingType;
import com.example.Backuni.entity.Category;
import com.example.Backuni.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
    boolean existsBuildingByNameAndStatus(String name,Status status);

    List<Building> findAllByStatus(Status status);
    

    List<Building> findByStatusAndCategory_Id(Status status,Long id);





    boolean existsByName(String name);

    Optional<Building> getByName(String name);
}
