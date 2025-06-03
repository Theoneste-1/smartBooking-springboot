package com.example.smartbooking.controllers;

import com.example.smartbooking.models.AuditLog;
import com.example.smartbooking.services.admin.AuditLogService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@PreAuthorize("hasRole('ADMIN')")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<Page<AuditLog>> getAllAuditLogs(
            Pageable pageable) {
        return ResponseEntity.ok(auditLogService.getAllAuditLogs(pageable));
    }

    @GetMapping("/admin/{adminId}")
    public ResponseEntity<Page<AuditLog>> getAdminAuditLogs(@PathVariable Long adminId, Pageable pageable) {
        return ResponseEntity.ok(auditLogService.getAdminAuditLogs(adminId, pageable));
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    public ResponseEntity<Page<AuditLog>> getEntityAuditLogs(
            @PathVariable String entityType,
            @PathVariable Long entityId,
            Pageable pageable) {
        return ResponseEntity.ok(auditLogService.getEntityAuditLogs(entityType, entityId, pageable));
    }

    @GetMapping("/date-range")
    public ResponseEntity<Page<AuditLog>> getAuditLogsByDateRange(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            Pageable pageable) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByDateRange(start, end, pageable));
    }
}