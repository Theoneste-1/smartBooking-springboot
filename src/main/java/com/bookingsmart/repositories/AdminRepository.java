package com.bookingsmart.repositories;

import com.bookingsmart.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    List<Admin> findByDepartment(String department);

    @Query("SELECT a FROM Admin a WHERE a.isActive = true")
    List<Admin> findAllActive();

    @Query("SELECT a FROM Admin a WHERE a.department = :department AND a.isActive = true")
    List<Admin> findActiveByDepartment(@Param("department") String department);

    @Query("SELECT COUNT(a) FROM Admin a WHERE a.department = :department")
    long countByDepartment(@Param("department") String department);
}