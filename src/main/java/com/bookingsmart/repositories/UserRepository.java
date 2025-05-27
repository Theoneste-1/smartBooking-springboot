package com.bookingsmart.repositories;

import com.bookingsmart.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.isActive = true")
    List<User> findAllActive();

    @Query("SELECT u FROM User u WHERE u.isVerified = true")
    List<User> findAllVerified();

    @Query("SELECT u FROM User u WHERE u.lastLogin < :date")
    List<User> findInactiveUsers(@Param("date") LocalDateTime date);

    @Query("SELECT u FROM User u WHERE u.createdAt >= :startDate AND u.createdAt <= :endDate")
    List<User> findUsersByRegistrationDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :startDate AND u.createdAt <= :endDate")
    long countUsersByRegistrationDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT u FROM User u WHERE u.isActive = false AND u.updatedAt < :date")
    List<User> findDeletedUsers(@Param("date") LocalDateTime date);
}