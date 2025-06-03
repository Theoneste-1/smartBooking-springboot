package com.example.smartbooking.controllers;

import com.example.smartbooking.dto.request.ChangePasswordRequest;
import com.example.smartbooking.dto.request.LoginRequest;
import com.example.smartbooking.dto.request.RegisterRequest;
import com.example.smartbooking.dto.response.AuthResponse;
import com.example.smartbooking.exceptions.custom.*;
import com.example.smartbooking.services.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) throws EmailValidationException {
        return ResponseEntity.ok(authService.login(loginRequest.getUsername(), loginRequest.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest)
            throws UsernameConflictException, EmailConflictException, PasswordValidationException,
            PhoneNumberException {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        authService.changePassword(request.getUserId(), request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }

}