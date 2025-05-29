package com.bookingsmart.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "professionals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Professional extends User {

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "business_address")
    private String businessAddress;

    @Column(name = "business_phone")
    private String businessPhone;

    @Column(name = "business_email")
    private String businessEmail;

    @Column(name = "professional_title")
    private String professionalTitle;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "hourly_rate")
    private Double hourlyRate;

    @Column(name = "is_verified")
    private boolean isVerified = false;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "admin_id") // Foreign key in Professional table
    private Admin admin;

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL)
    private Set<Service> services = new HashSet<>();

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL)
    private Set<Availability> availabilities = new HashSet<>();

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL)
    private Set<Appointment> appointments = new HashSet<>();

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL)
    private Set<Review> reviews = new HashSet<>();
}