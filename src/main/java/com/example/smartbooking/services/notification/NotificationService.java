package com.example.smartbooking.services.notification;

import com.example.smartbooking.models.Notification;
import com.example.smartbooking.models.Appointment;
import com.example.smartbooking.models.Payment;
import com.example.smartbooking.repositories.NotificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    // private final EmailService emailService;
    // private final SmsService smsService;
    // private final PushNotificationService pushNotificationService;

    // public NotificationService(NotificationRepository notificationRepository,
    // EmailService emailService,
    // SmsService smsService,
    // PushNotificationService pushNotificationService) {
    // this.notificationRepository = notificationRepository;
    // this.emailService = emailService;
    // this.smsService = smsService;
    // this.pushNotificationService = pushNotificationService;
    // }

    @Transactional
    public void sendAppointmentConfirmation(Appointment appointment) {
        Notification notification = new Notification();
        notification.setUser(appointment.getClient());
        notification.setTitle("Appointment Confirmed");
        notification.setMessage("Your appointment has been confirmed for " + appointment.getStartTime());
        notification.setType(Notification.NotificationType.APPOINTMENT_REMINDER);

        notificationRepository.save(notification);
        // emailService.sendAppointmentConfirmationEmail(appointment);
        // smsService.sendAppointmentConfirmationSms(appointment);
        // pushNotificationService.sendAppointmentConfirmationPush(appointment);
    }

    @Transactional
    public void sendAppointmentStatusUpdate(Appointment appointment) {
        Notification notification = new Notification();
        notification.setUser(appointment.getClient());
        notification.setTitle("Appointment Status Updated");
        notification.setMessage("Your appointment status has been updated to " + appointment.getStatus());
        notification.setType(Notification.NotificationType.APPOINTMENT_REMINDER);

        notificationRepository.save(notification);
        // emailService.sendAppointmentStatusUpdateEmail(appointment);
    }

    @Transactional
    public void sendPaymentConfirmation(Payment payment) {
        Notification notification = new Notification();
        notification.setUser(payment.getAppointment().getClient());
        notification.setTitle("Payment Confirmed");
        notification.setMessage("Your payment of " + payment.getAmount() + " has been confirmed");
        notification.setType(Notification.NotificationType.PAYMENT_CONFIRMATION);

        notificationRepository.save(notification);
        // emailService.sendPaymentConfirmationEmail(payment);
    }

    public Page<Notification> getUserNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByUserId(userId, pageable);
    }

    public Page<Notification> getUnreadNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByUserIdAndIsRead(userId, false, pageable);
    }

    @Transactional
    public void markNotificationAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public void deleteNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notificationRepository.delete(notification);
    }

    public boolean isNotificationOwner(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        return notification.getUser().getId().equals(userId);
    }

    @Transactional
    public void deleteAllUserNotifications(Long userId) {
        notificationRepository.deleteByUserId(userId);
    }

    @Transactional
    public void markAllNotificationsAsRead(Long userId) {
        notificationRepository.markAllAsReadByUserId(userId);
    }
}