package com.bookingsmart.services.admin;

import com.bookingsmart.models.Admin;
import com.bookingsmart.models.Admin.AdminRole;
import com.bookingsmart.repositories.AdminRepository;
import com.bookingsmart.services.admin.AuditLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final AuditLogService auditLogService;

    public AdminService(AdminRepository adminRepository, AuditLogService auditLogService) {
        this.adminRepository = adminRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public Admin createAdmin(Admin admin) {
        Admin savedAdmin = adminRepository.save(admin);
        auditLogService.createAuditLog(
                admin,
                "CREATE_ADMIN",
                "ADMIN",
                savedAdmin.getId(),
                "Created new admin user",
                null);
        return savedAdmin;
    }

    @Transactional
    public Admin updateAdmin(Long id, Admin adminDetails) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        admin.setRole(adminDetails.getRole());
        admin.setDepartment(adminDetails.getDepartment());

        Admin updatedAdmin = adminRepository.save(admin);
        auditLogService.createAuditLog(
                admin,
                "UPDATE_ADMIN",
                "ADMIN",
                updatedAdmin.getId(),
                "Updated admin details",
                null);
        return updatedAdmin;
    }

    @Transactional
    public void deleteAdmin(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        admin.setActive(false);
        adminRepository.save(admin);

        auditLogService.createAuditLog(
                admin,
                "DELETE_ADMIN",
                "ADMIN",
                admin.getId(),
                "Deactivated admin account",
                null);
    }

    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public List<Admin> getAdminsByRole(AdminRole role) {
        return adminRepository.findByRole(role);
    }

    public List<Admin> getAdminsByDepartment(String department) {
        return adminRepository.findByDepartment(department);
    }

    @Transactional
    public void changeAdminRole(Long id, AdminRole newRole) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        AdminRole oldRole = AdminRole.SUPER_ADMIN;
        admin.setRole(newRole);

        Admin updatedAdmin = adminRepository.save(admin);
        auditLogService.createAuditLog(
                admin,
                "CHANGE_ROLE",
                "ADMIN",
                updatedAdmin.getId(),
                String.format("Changed role from %s to %s", oldRole, newRole),
                null);
    }
}