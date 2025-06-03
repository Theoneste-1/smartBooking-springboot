package com.example.smartbooking.repositories;

import com.example.smartbooking.models.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

        Page<AuditLog> findByAdminId(Long adminId, Pageable pageable);

        Page<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId, Pageable pageable);

        Page<AuditLog> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

        Page<AuditLog> findByAction(String action, Pageable pageable);

        Page<AuditLog> findByEntityType(String entityType, Pageable pageable);

        Page<AuditLog> findByIpAddress(String ipAddress, Pageable pageable);

        @Query("SELECT a FROM AuditLog a WHERE " +
                        "(:adminId IS NULL OR a.admin.id = :adminId) AND " +
                        "(:action IS NULL OR a.action = :action) AND " +
                        "(:entityType IS NULL OR a.entityType = :entityType) AND " +
                        "(:startDate IS NULL OR a.createdAt >= :startDate) AND " +
                        "(:endDate IS NULL OR a.createdAt <= :endDate)")
        Page<AuditLog> searchAuditLogs(@Param("criteria") Map<String, Object> criteria, Pageable pageable);

        long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

        @Query("SELECT COUNT(DISTINCT a.admin.id) FROM AuditLog a WHERE a.createdAt BETWEEN :start AND :end")
        long countDistinctAdminByCreatedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

        @Query("SELECT COUNT(DISTINCT a.entityId) FROM AuditLog a WHERE a.createdAt BETWEEN :start AND :end")
        long countDistinctEntityByCreatedAtBetween(@Param("start") LocalDateTime start,
                        @Param("end") LocalDateTime end);

        @Query("SELECT a.action, COUNT(a) FROM AuditLog a WHERE a.createdAt BETWEEN :start AND :end GROUP BY a.action")
        Map<String, Long> countByActionAndCreatedAtBetween(@Param("start") LocalDateTime start,
                        @Param("end") LocalDateTime end);

        @Query("SELECT a.entityType, COUNT(a) FROM AuditLog a WHERE a.createdAt BETWEEN :start AND :end GROUP BY a.entityType")
        Map<String, Long> countByEntityTypeAndCreatedAtBetween(@Param("start") LocalDateTime start,
                        @Param("end") LocalDateTime end);

        void deleteByCreatedAtBefore(LocalDateTime before);

        @Query("SELECT a FROM AuditLog a ORDER BY a.createdAt DESC LIMIT :limit")
        List<AuditLog> findTopByOrderByCreatedAtDesc(@Param("limit") int limit);

        @Query("SELECT COUNT(a) FROM AuditLog a WHERE a.action = :action AND a.createdAt BETWEEN :start AND :end")
        long countByActionAndCreatedAtBetween(@Param("action") String action,
                        @Param("start") LocalDateTime start,
                        @Param("end") LocalDateTime end);
}