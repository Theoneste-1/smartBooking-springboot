package com.bookingsmart.repositories;

import com.bookingsmart.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByProfessionalId(Long professionalId, Pageable pageable);

    Page<Review> findByClientId(Long clientId, Pageable pageable);

    Page<Review> findByRating(Integer rating, Pageable pageable);

    boolean existsByAppointmentId(Long appointmentId);

    boolean existsByClientIdAndAppointmentId(Long clientId, Long appointmentId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.professional.id = :professionalId")
    Double calculateAverageRatingByProfessionalId(@Param("professionalId") Long professionalId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.professional.id = :professionalId")
    Long countByProfessionalId(@Param("professionalId") Long professionalId);

    @Query("SELECT r.rating as rating, COUNT(r) as count FROM Review r WHERE r.professional.id = :professionalId GROUP BY r.rating")
    Map<Integer, Long> getRatingDistributionByProfessionalId(@Param("professionalId") Long professionalId);

    @Query("SELECT r FROM Review r WHERE " +
            "LOWER(r.comment) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "r.rating = :keyword")
    Page<Review> searchReviews(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT r FROM Review r ORDER BY r.createdAt DESC LIMIT :limit")
    List<Review> findTopByOrderByCreatedAtDesc(@Param("limit") int limit);
}