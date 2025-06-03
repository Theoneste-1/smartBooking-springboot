package com.example.smartbooking.controllers;

import com.example.smartbooking.exceptions.custom.UserNotFoundException;
import com.example.smartbooking.models.Appointment;
import com.example.smartbooking.models.User;
import com.example.smartbooking.services.booking.AppointmentService;
import com.example.smartbooking.services.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserService userService;

    public AppointmentController(AppointmentService appointmentService, UserService userService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
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
    public ResponseEntity<Appointment> updateStatus(@PathVariable Long id,
            @RequestBody Appointment.AppointmentStatus status) {
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


    @GetMapping("/my-appointments")
    public ResponseEntity<List<Appointment>> getCurrentUserAppointments() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName())
                .orElseThrow(()-> new UserNotFoundException("User not found")); // Assumes user ID is stored as the username/principal
        return ResponseEntity.ok(appointmentService.getClientAppointments(user.getId()));
    }
}