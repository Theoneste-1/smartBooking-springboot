package com.example.smartbooking.controllers;

import com.example.smartbooking.models.Payment;
import com.example.smartbooking.services.payment.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Payment> processPayment(@RequestBody Payment payment) {
        return ResponseEntity.ok(paymentService.processPayment(payment));
    }

    @PostMapping("/{id}/refund")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Payment> refundPayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.refundPayment(id));
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasRole('ADMIN') or #clientId == authentication.principal.id")
    public ResponseEntity<List<Payment>> getClientPayments(@PathVariable Long clientId) {
        return ResponseEntity.ok(paymentService.getClientPayments(clientId));
    }

    @GetMapping("/professional/{professionalId}")
    @PreAuthorize("hasRole('ADMIN') or #professionalId == authentication.principal.id")
    public ResponseEntity<List<Payment>> getProfessionalPayments(@PathVariable Long professionalId) {
        return ResponseEntity.ok(paymentService.getProfessionalPayments(professionalId));
    }
}