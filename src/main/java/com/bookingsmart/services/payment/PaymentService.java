package com.bookingsmart.services.payment;

import com.bookingsmart.models.Payment;
import com.bookingsmart.repositories.PaymentRepository;
import com.bookingsmart.services.notification.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final StripeService stripeService;
    private final NotificationService notificationService;

    public PaymentService(PaymentRepository paymentRepository,
            StripeService stripeService,
            NotificationService notificationService) {
        this.paymentRepository = paymentRepository;
        this.stripeService = stripeService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Payment processPayment(Payment payment) {
        // Process payment through Stripe
//        String transactionId = stripeService.processPayment(payment);
        String transactionId = "123456789";
        payment.setTransactionId(transactionId);
        payment.setStatus(Payment.PaymentStatus.COMPLETED);

        Payment savedPayment = paymentRepository.save(payment);
        notificationService.sendPaymentConfirmation(savedPayment);

        return savedPayment;
    }

    @Transactional
    public Payment refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

//        stripeService.processRefund(payment);
        payment.setStatus(Payment.PaymentStatus.REFUNDED);

        return paymentRepository.save(payment);
    }

    public List<Payment> getClientPayments(Long clientId) {
        return paymentRepository.findByClientId(clientId);
    }

    public List<Payment> getProfessionalPayments(Long professionalId) {
        return paymentRepository.findByProfessionalId(professionalId);
    }
}