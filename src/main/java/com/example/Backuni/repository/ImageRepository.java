package com.example.Backuni.repository;

import com.example.Backuni.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByBuilding_id(Long id);

    List<Image> findByBuilding_IdAndCabinet_Id(Long buildId,Long cabinetId);





}
