package com.bookingsmart.util;

import com.bookingsmart.exceptions.custom.EmailConflictException;
import com.bookingsmart.exceptions.custom.PasswordValidationException;
import com.bookingsmart.exceptions.custom.PhoneNumberException;
import com.bookingsmart.exceptions.custom.UsernameConflictException;
import com.bookingsmart.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrationValidation {

    @Autowired
    private PasswordValidatorService passwordValidatorService;

    @Autowired
    private PhoneNumberValidatorService phoneNumberValidatorService;

    @Autowired
    private UserRepository userRepository;

    public boolean validateUsername(String username) throws UsernameConflictException {
            boolean usernameExists = userRepository.existsByUsername(username);
            if (usernameExists) {
                throw new UsernameConflictException("User name already exist");
            }
        return true;
    }

    public boolean validateEmail(String email) throws EmailConflictException {
            boolean emailExists = userRepository.existsByEmail(email);
            if (emailExists) {
                throw new EmailConflictException("Email already exist");
            }
            return true;
    }

    public boolean validatePassword(String password) throws PasswordValidationException {
      return  passwordValidatorService.validatePassword(password);
    }

    public boolean validatePhoneNumber(String PhoneNumber) throws PhoneNumberException {
      return phoneNumberValidatorService.validatePhoneNumber(PhoneNumber);
    }
}
