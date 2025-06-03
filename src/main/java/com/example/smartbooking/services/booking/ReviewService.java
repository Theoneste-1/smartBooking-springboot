package com.example.smartbooking.services.booking;

import com.example.smartbooking.models.Review;
import com.example.smartbooking.models.Appointment;
import com.example.smartbooking.models.Client;
import com.example.smartbooking.models.Professional;
import com.example.smartbooking.repositories.ReviewRepository;
import com.example.smartbooking.repositories.AppointmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AppointmentRepository appointmentRepository;

    public ReviewService(ReviewRepository reviewRepository, AppointmentRepository appointmentRepository) {
        this.reviewRepository = reviewRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public Review createReview(Review review) {
        // Validate that the appointment exists and belongs to the client
        Appointment appointment = appointmentRepository.findById(review.getAppointment().getId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (!appointment.getClient().getId().equals(review.getClient().getId())) {
            throw new RuntimeException("Client can only review their own appointments");
        }

        // Check if appointment is completed
        if (appointment.getStatus() != Appointment.AppointmentStatus.COMPLETED) {
            throw new RuntimeException("Can only review completed appointments");
        }

        // Check if review already exists for this appointment
        if (reviewRepository.existsByAppointmentId(appointment.getId())) {
            throw new RuntimeException("Review already exists for this appointment");
        }

        // Validate rating
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
    }

    public Page<Review> getProfessionalReviews(Long professionalId, Pageable pageable) {
        return reviewRepository.findByProfessionalId(professionalId, pageable);
    }

    public Page<Review> getClientReviews(Long clientId, Pageable pageable) {
        return reviewRepository.findByClientId(clientId, pageable);
    }

    public Page<Review> getReviewsByRating(Integer rating, Pageable pageable) {
        return reviewRepository.findByRating(rating, pageable);
    }

    @Transactional
    public Review updateReview(Long id, Review reviewDetails) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // Validate rating
        if (reviewDetails.getRating() < 1 || reviewDetails.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        review.setRating(reviewDetails.getRating());
        review.setComment(reviewDetails.getComment());
        review.setUpdatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        reviewRepository.delete(review);
    }

    public boolean isReviewOwner(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        return review.getClient().getId().equals(userId);
    }

    public Map<String, Object> getProfessionalReviewStats(Long professionalId) {
        Map<String, Object> stats = new HashMap<>();

        Double averageRating = reviewRepository.calculateAverageRatingByProfessionalId(professionalId);
        Long totalReviews = reviewRepository.countByProfessionalId(professionalId);
        Map<Integer, Long> ratingDistribution = reviewRepository.getRatingDistributionByProfessionalId(professionalId);

        stats.put("averageRating", averageRating);
        stats.put("totalReviews", totalReviews);
        stats.put("ratingDistribution", ratingDistribution);

        return stats;
    }

    public Page<Review> searchReviews(String keyword, Pageable pageable) {
        return reviewRepository.searchReviews(keyword, pageable);
    }

    public List<Review> getRecentReviews(int limit) {
        return reviewRepository.findTopByOrderByCreatedAtDesc(limit);
    }

    public boolean hasClientReviewedAppointment(Long clientId, Long appointmentId) {
        return reviewRepository.existsByClientIdAndAppointmentId(clientId, appointmentId);
    }
}