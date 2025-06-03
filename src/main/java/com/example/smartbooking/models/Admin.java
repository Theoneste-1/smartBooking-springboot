package com.example.smartbooking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "admins")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Admin extends User {

    @Column(name = "department")
    private String department;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private Set<AuditLog> auditLogs = new HashSet<>();
//
//    @OneToOne(mappedBy = "admin", fetch = FetchType.LAZY)
//    @JsonIgnore
//    private Professional professional;
}