package com.example.smartbooking.repositories;

import com.example.smartbooking.models.Payment;
import com.example.smartbooking.models.Payment.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByClientId(Long clientId);

    List<Payment> findByProfessionalId(Long professionalId);

    List<Payment> findByStatus(PaymentStatus status);

    Payment findByTransactionId(String transactionId);
}