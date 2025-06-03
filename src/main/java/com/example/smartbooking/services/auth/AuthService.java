package com.example.smartbooking.services.auth;

import com.example.smartbooking.dto.request.RegisterRequest;
import com.example.smartbooking.dto.response.AuthResponse;
import com.example.smartbooking.exceptions.custom.*;
import com.example.smartbooking.models.User;
import com.example.smartbooking.repositories.UserRepository;

import com.example.smartbooking.util.LoginValidation;
import com.example.smartbooking.util.PhoneNumberValidatorService;
import com.example.smartbooking.util.RegistrationValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service

public class AuthService {

    private final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final RegistrationValidation registrationValidation;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final PhoneNumberValidatorService phoneNumberValidatorService;

    public AuthService(RegistrationValidation registrationValidation, UserRepository userRepository,
            PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService,
            PhoneNumberValidatorService phoneNumberValidatorService, LoginValidation loginValidation) {
        this.registrationValidation = registrationValidation;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.phoneNumberValidatorService = phoneNumberValidatorService;
    }

    @Transactional
    public String register(RegisterRequest registerRequest) throws UsernameConflictException, EmailConflictException,
            PhoneNumberException, PasswordValidationException {

        boolean isUsernameValidated = registrationValidation.validateUsername(registerRequest.getUsername());
        boolean isEmailValidated = registrationValidation.validateEmail(registerRequest.getEmail());
        boolean isPasswordValidated = registrationValidation.validatePassword(registerRequest.getPassword());
        boolean isPhoneNumberValidated = phoneNumberValidatorService
                .validatePhoneNumber(registerRequest.getPhoneNumber());

        // Create new user
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setRole(registerRequest.getRole());
        user.setActive(true);
        user.setVerified(false);
        user.setAgreedToTerms(registerRequest.isAgreedToTerms());
        // Save user
        userRepository.save(user);

        return "User Registered Successfully";

    }

    @Transactional
    public AuthResponse login(String usernameOrEmail, String password) throws EmailValidationException {

        String emailForAuth;
        User user;
        if (isEmail(usernameOrEmail)) {
            emailForAuth = usernameOrEmail;
            log.info("Email verified", emailForAuth);
            user = userRepository.findByEmail(usernameOrEmail)
                    .orElseThrow(() -> new EmailValidationException("Email not found: " + usernameOrEmail));
        } else {
            log.info("username: ", usernameOrEmail);
            user = userRepository.findByUsername(usernameOrEmail)
                    .orElseThrow(() -> new EmailValidationException("Username not found: " + usernameOrEmail));
            emailForAuth = user.getEmail();
            if (emailForAuth == null) {
                throw new EmailValidationException("No email associated with username: " + usernameOrEmail);
            }
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(emailForAuth, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String token = tokenService.generateToken(authentication);
        String refreshToken = tokenService.generateRefreshToken(authentication);
        return new AuthResponse(token, refreshToken, user.getUsername(), user.getRole());
    }

    @Transactional
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Invalid old password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private boolean isEmail(String input) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return input != null && input.matches(emailRegex);
    }
}