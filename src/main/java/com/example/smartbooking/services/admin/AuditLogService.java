package com.example.smartbooking.services.admin;

import com.example.smartbooking.models.AuditLog;
import com.example.smartbooking.models.Admin;
import com.example.smartbooking.repositories.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    /**
     * Create a new audit log entry
     */
    @Transactional
    public void createAuditLog(Admin admin, String action, String entityType, Long entityId,
            String details, String ipAddress) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAdmin(admin);
        auditLog.setAction(action);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setDetails(details);
        auditLog.setIpAddress(ipAddress);
        auditLog.setCreatedAt(LocalDateTime.now());

        auditLogRepository.save(auditLog);
    }

    /**
     * Get all audit logs with pagination
     */
    public Page<AuditLog> getAllAuditLogs(Pageable pageable) {
        return auditLogRepository.findAll(pageable);
    }

    /**
     * Get audit logs for a specific admin
     */
    public Page<AuditLog> getAdminAuditLogs(Long adminId, Pageable pageable) {
        return auditLogRepository.findByAdminId(adminId, pageable);
    }

    /**
     * Get audit logs for a specific entity
     */
    public Page<AuditLog> getEntityAuditLogs(String entityType, Long entityId, Pageable pageable) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId, pageable);
    }

    /**
     * Get audit logs within a date range
     */
    public Page<AuditLog> getAuditLogsByDateRange(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return auditLogRepository.findByCreatedAtBetween(start, end, pageable);
    }

    /**
     * Get audit logs by action type
     */
    public Page<AuditLog> getAuditLogsByAction(String action, Pageable pageable) {
        return auditLogRepository.findByAction(action, pageable);
    }

    /**
     * Get audit logs by entity type
     */
    public Page<AuditLog> getAuditLogsByEntityType(String entityType, Pageable pageable) {
        return auditLogRepository.findByEntityType(entityType, pageable);
    }

    /**
     * Get audit logs by IP address
     */
    public Page<AuditLog> getAuditLogsByIpAddress(String ipAddress, Pageable pageable) {
        return auditLogRepository.findByIpAddress(ipAddress, pageable);
    }

    /**
     * Search audit logs with multiple criteria
     */
    public Page<AuditLog> searchAuditLogs(Map<String, Object> criteria, Pageable pageable) {
        return auditLogRepository.searchAuditLogs(criteria, pageable);
    }

    /**
     * Get audit log statistics
     */
    public Map<String, Object> getAuditLogStatistics(LocalDateTime start, LocalDateTime end) {
        Map<String, Object> statistics = new HashMap<>();

        statistics.put("totalLogs", auditLogRepository.countByCreatedAtBetween(start, end));
        statistics.put("uniqueAdmins", auditLogRepository.countDistinctAdminByCreatedAtBetween(start, end));
        statistics.put("uniqueEntities", auditLogRepository.countDistinctEntityByCreatedAtBetween(start, end));
        statistics.put("actionsByType", auditLogRepository.countByActionAndCreatedAtBetween(start, end));
        statistics.put("entitiesByType", auditLogRepository.countByEntityTypeAndCreatedAtBetween(start, end));

        return statistics;
    }

    /**
     * Export audit logs to CSV
     */
    public String exportAuditLogsToCsv(LocalDateTime start, LocalDateTime end, int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<AuditLog> logs = auditLogRepository.findByCreatedAtBetween(start, end, pageable);
        StringBuilder csv = new StringBuilder();

        // Add CSV header
        csv.append("ID,Admin,Action,Entity Type,Entity ID,Details,IP Address,Created At\n");

        // Add data rows
        for (AuditLog log : logs) {
            csv.append(String.format("%d,%s,%s,%s,%d,%s,%s,%s\n",
                    log.getId(),
                    log.getAdmin().getUsername(),
                    log.getAction(),
                    log.getEntityType(),
                    log.getEntityId(),
                    log.getDetails(),
                    log.getIpAddress(),
                    log.getCreatedAt()));
        }

        return csv.toString();
    }

    /**
     * Clean up old audit logs
     */
    @Transactional
    public void cleanupOldAuditLogs(LocalDateTime before) {
        auditLogRepository.deleteByCreatedAtBefore(before);
    }

    /**
     * Get audit log by ID
     */
    public AuditLog getAuditLogById(Long id) {
        return auditLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit log not found"));
    }

    /**
     * Get recent audit logs
     */
    public List<AuditLog> getRecentAuditLogs(int limit) {
        return auditLogRepository.findTopByOrderByCreatedAtDesc(limit);
    }

    /**
     * Get audit logs for system health monitoring
     */
    public Map<String, Object> getSystemHealthMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourAgo = now.minusHours(1);
        LocalDateTime oneDayAgo = now.minusDays(1);

        metrics.put("logsLastHour", auditLogRepository.countByCreatedAtBetween(oneHourAgo, now));
        metrics.put("logsLastDay", auditLogRepository.countByCreatedAtBetween(oneDayAgo, now));
        metrics.put("activeAdminsLastHour", auditLogRepository.countDistinctAdminByCreatedAtBetween(oneHourAgo, now));
        metrics.put("errorCountLastHour",
                auditLogRepository.countByActionAndCreatedAtBetween("ERROR", oneHourAgo, now));

        return metrics;
    }
}