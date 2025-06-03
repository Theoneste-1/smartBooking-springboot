package com.example.smartbooking.util;

import com.example.smartbooking.exceptions.custom.PasswordValidationException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PasswordValidatorService {
    public boolean validatePassword(String password) throws PasswordValidationException {
        List<String> errors = new ArrayList<>();

        if (password == null || password.trim().isEmpty()) {
            errors.add("Password cannot be null or empty");
        } else {
            if (password.length() < 6) {
                errors.add("Password must be at least 6 characters long");
            }
            if (!password.matches(".*[a-zA-Z].*")) {
                errors.add("Password must contain at least one letter");
            }
            if (!password.matches(".*[!@#$%^&*].*")) {
                errors.add("Password must contain at least one symbol (e.g., !@#$%^&*)");
            }
        }
        if (!errors.isEmpty()) {
            throw new PasswordValidationException(String.join("; ", errors));
        } else {
            return true;
        }

    }

}
