package com.bookingsmart.controllers;

import com.bookingsmart.models.Review;
import com.bookingsmart.services.booking.ReviewService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        return ResponseEntity.ok(reviewService.createReview(review));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping("/professional/{professionalId}")
    public ResponseEntity<Page<Review>> getProfessionalReviews(@PathVariable Long professionalId, Pageable pageable) {
        return ResponseEntity.ok(reviewService.getProfessionalReviews(professionalId, pageable));
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasRole('ADMIN') or #clientId == authentication.principal.id")
    public ResponseEntity<Page<Review>> getClientReviews(@PathVariable Long clientId, Pageable pageable) {
        return ResponseEntity.ok(reviewService.getClientReviews(clientId, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @reviewService.isReviewOwner(#id, authentication.principal.id)")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review reviewDetails) {
        return ResponseEntity.ok(reviewService.updateReview(id, reviewDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @reviewService.isReviewOwner(#id, authentication.principal.id)")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok().build();
    }
}