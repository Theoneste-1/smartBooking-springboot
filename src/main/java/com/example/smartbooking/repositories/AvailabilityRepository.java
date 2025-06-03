package com.example.smartbooking.repositories;

import com.example.smartbooking.models.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByProfessionalId(Long professionalId);

    List<Availability> findByProfessionalIdAndDayOfWeek(Long professionalId, DayOfWeek dayOfWeek);

    List<Availability> findByProfessionalIdAndIsAvailable(Long professionalId, boolean isAvailable);
}