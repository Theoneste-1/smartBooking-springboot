package com.bookingsmart.services.auth;

import com.bookingsmart.models.User;
import com.bookingsmart.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
//    private final EmailService emailService;

    public PasswordResetService(UserRepository userRepository,
            PasswordEncoder passwordEncoder){
//            EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
//        this.emailService = emailService;
    }

    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String resetToken = UUID.randomUUID().toString();
        // Store reset token and expiration in user entity or separate table
        // Send email with reset link
//        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
    }

    @Transactional
    public void resetPassword(String token, String newPassword, String email) {
        // Validate token and expiration
        // Update password
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}