package com.bookingsmart.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "admins")
public class Admin extends User {

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private AdminRole role;

    @Column(name = "department")
    private String department;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private Set<AuditLog> auditLogs = new HashSet<>();

    public enum AdminRole {
        SUPER_ADMIN,
        SYSTEM_ADMIN,
        CONTENT_ADMIN,
        SUPPORT_ADMIN
    }

    // Getters and Setters
    public String getRole() {
        return role.name();
    }

    public void setRole(AdminRole role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Set<AuditLog> getAuditLogs() {
        return auditLogs;
    }

    public void setAuditLogs(Set<AuditLog> auditLogs) {
        this.auditLogs = auditLogs;
    }
}