package com.bookingsmart.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "professionals")
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

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL)
    private Set<Service> services = new HashSet<>();

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL)
    private Set<Availability> availabilities = new HashSet<>();

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL)
    private Set<Appointment> appointments = new HashSet<>();

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL)
    private Set<Review> reviews = new HashSet<>();

    // Getters and Setters
    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public String getBusinessEmail() {
        return businessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }

    public String getProfessionalTitle() {
        return professionalTitle;
    }

    public void setProfessionalTitle(String professionalTitle) {
        this.professionalTitle = professionalTitle;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }

    public Set<Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(Set<Availability> availabilities) {
        this.availabilities = availabilities;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }
}