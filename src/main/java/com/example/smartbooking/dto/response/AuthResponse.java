package com.example.smartbooking.dto.response;

import com.example.smartbooking.models.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String refreshToken;
    private String username;
    private UserRole role;
}