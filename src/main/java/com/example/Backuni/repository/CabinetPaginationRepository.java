package com.example.Backuni.repository;

import com.example.Backuni.entity.Building;
import com.example.Backuni.entity.Cabinet;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CabinetPaginationRepository extends PagingAndSortingRepository<Cabinet,Long> {
}