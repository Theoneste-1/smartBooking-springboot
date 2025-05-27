package com.bookingsmart.controllers;

import com.bookingsmart.dto.request.ChangePasswordRequest;
import com.bookingsmart.dto.request.LoginRequest;
import com.bookingsmart.dto.request.RegisterRequest;
import com.bookingsmart.dto.response.AuthResponse;
import com.bookingsmart.services.auth.AuthService;
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
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        String token = authService.register(registerRequest);
        return ResponseEntity.ok(new AuthResponse(token));
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