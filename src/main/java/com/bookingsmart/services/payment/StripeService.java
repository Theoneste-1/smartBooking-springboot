package com.bookingsmart.services.payment;

import org.springframework.stereotype.Service;

//
//import com.bookingsmart.models.Payment;
//import com.stripe.Stripe;
//import com.stripe.model.Charge;
//import com.stripe.model.Refund;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.Map;
//
@Service
public class StripeService {
//
}
//    @Value("${stripe.secret.key}")
//    private String stripeSecretKey;
//
//    public String processPayment(Payment payment) {
//        Stripe.apiKey = stripeSecretKey;
//
//        Map<String, Object> chargeParams = new HashMap<>();
//        chargeParams.put("amount", payment.getAmount().multiply(new BigDecimal("100")).longValue());
//        chargeParams.put("currency", payment.getCurrency());
//        chargeParams.put("source", payment.getPaymentMethod());
//        chargeParams.put("description", "Payment for appointment #" + payment.getAppointment().getId());
//
//        try {
//            Charge charge = Charge.create(chargeParams);
//            return charge.getId();
//        } catch (Exception e) {
//            throw new RuntimeException("Payment processing failed", e);
//        }
//    }
//
//    public void processRefund(Payment payment) {
//        Stripe.apiKey = stripeSecretKey;
//
//        Map<String, Object> refundParams = new HashMap<>();
//        refundParams.put("charge", payment.getTransactionId());
//
//        try {
//            Refund.create(refundParams);
//        } catch (Exception e) {
//            throw new RuntimeException("Refund processing failed", e);
//        }
//    }
//}