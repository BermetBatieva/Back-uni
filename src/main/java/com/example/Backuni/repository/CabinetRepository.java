package com.example.Backuni.repository;

import com.example.Backuni.entity.Building;
import com.example.Backuni.entity.Cabinet;
import com.example.Backuni.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CabinetRepository extends JpaRepository<Cabinet,Long> {

    boolean existsByNumber(Long n);

    List<Cabinet> findByBuilding_IdAndFloorNumberAndStatus(Long buildingId, Integer floorNum, Status status);


}
