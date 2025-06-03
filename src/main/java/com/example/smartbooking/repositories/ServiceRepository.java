package com.example.smartbooking.repositories;

import com.example.smartbooking.models.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByCategoryId(Long categoryId);

    List<Service> findByIsActive(boolean isActive);
}