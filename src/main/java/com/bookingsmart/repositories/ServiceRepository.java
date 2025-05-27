package com.bookingsmart.repositories;

import com.bookingsmart.models.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByProfessionalId(Long professionalId);

    List<Service> findByCategoryId(Long categoryId);

    List<Service> findByIsActive(boolean isActive);
}