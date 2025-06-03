package com.example.smartbooking.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "professional_id", referencedColumnName = "id", nullable = false)
    private Professional professional;

    @OneToMany(mappedBy = "category")
    private Set<Service> services = new HashSet<>();

    @Column(name = "is_active")
    private boolean isActive = true;
}