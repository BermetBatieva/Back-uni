package com.example.Backuni.repository;

import com.example.Backuni.entity.Building;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BuildingPaginationRepository extends PagingAndSortingRepository<Building,Long> {
}
