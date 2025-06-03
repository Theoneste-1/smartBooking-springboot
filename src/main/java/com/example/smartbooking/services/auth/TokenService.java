package com.example.smartbooking.services.auth;

import com.example.smartbooking.config.JwtConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final JwtConfig jwtConfig;

    public TokenService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(Authentication authentication) {
        return jwtConfig.generateToken(authentication);
    }

    public boolean validateToken(String token, String username) {
        return jwtConfig.validateToken(token, username);
    }

    public String getUsernameFromToken(String token) {
        return jwtConfig.extractUsername(token);
    }
    public String generateRefreshToken(Authentication authentication) { return jwtConfig.generateRefreshToken(authentication.getName());}
}