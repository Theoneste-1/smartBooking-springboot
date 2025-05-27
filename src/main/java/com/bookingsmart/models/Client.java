package com.bookingsmart.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "clients")
public class Client extends User {

    @Column(name = "address")
    private String address;

    @Column(name = "preferred_language")
    private String preferredLanguage;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Appointment> appointments = new HashSet<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Payment> payments = new HashSet<>();

    // Getters and Setters
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
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

    public Set<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }
}