package com.example.smartbooking.repositories;

import com.example.smartbooking.models.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessionalRepository extends JpaRepository<Professional, Long> {
    List<Professional> findByIsVerified(boolean isVerified);

    Optional<Professional> findByUserId(Long userId);
}