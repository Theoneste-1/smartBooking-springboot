package com.bookingsmart.repositories;

import com.bookingsmart.models.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessionalRepository extends JpaRepository<Professional, Long> {
    List<Professional> findByIsVerified(boolean isVerified);
}