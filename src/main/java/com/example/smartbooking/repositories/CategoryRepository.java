package com.example.smartbooking.repositories;

import com.example.smartbooking.models.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByIsActive(boolean isActive);

    Optional<Category> findByName(String name);

    boolean existsByName(String name);

    long countByIsActive(boolean isActive);

    @Query("SELECT c FROM Category c WHERE c.isActive = true AND SIZE(c.services) > 0")
    List<Category> findByIsActiveAndServicesIsNotEmpty(boolean isActive);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.services WHERE c.isActive = true")
    List<Category> findAllActiveWithServices();
}