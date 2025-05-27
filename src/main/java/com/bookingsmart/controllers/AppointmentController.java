package com.bookingsmart.controllers;

import com.bookingsmart.models.Appointment;
import com.bookingsmart.services.booking.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentService.createAppointment(appointment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long id, @RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, appointment));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Appointment> updateStatus(@PathVariable Long id, @RequestBody Appointment.AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, status));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Appointment>> getClientAppointments(@PathVariable Long clientId) {
        return ResponseEntity.ok(appointmentService.getClientAppointments(clientId));
    }

    @GetMapping("/professional/{professionalId}")
    public ResponseEntity<List<Appointment>> getProfessionalAppointments(@PathVariable Long professionalId) {
        return ResponseEntity.ok(appointmentService.getProfessionalAppointments(professionalId));
    }
}