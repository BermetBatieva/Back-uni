package com.example.Backuni.repository;


import com.example.Backuni.entity.LinkToMap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkToMapRepository extends JpaRepository<LinkToMap, Long> {

    LinkToMap findByBuilding_Id(Long id);
}
