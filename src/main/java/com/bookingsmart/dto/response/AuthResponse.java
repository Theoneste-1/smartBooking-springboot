package com.bookingsmart.dto.response;

import com.bookingsmart.models.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private UserRole role;
}