package com.example.smartbooking.util;

import com.example.smartbooking.exceptions.custom.EmailConflictException;
import com.example.smartbooking.exceptions.custom.PasswordValidationException;
import com.example.smartbooking.exceptions.custom.PhoneNumberException;
import com.example.smartbooking.exceptions.custom.UsernameConflictException;
import com.example.smartbooking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrationValidation {

  @Autowired
  private PasswordValidatorService passwordValidatorService;

  @Autowired
  private PhoneNumberValidatorService phoneNumberValidatorService;

  @Autowired
  private LoginValidation loginValidation;

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
    boolean isValidEmail = loginValidation.validateEmail(email);
    boolean emailExists = userRepository.existsByEmail(email);
    if (emailExists) {
      throw new EmailConflictException("Email already exist");
    }
    return true;
  }

  public boolean validatePassword(String password) throws PasswordValidationException {
    return passwordValidatorService.validatePassword(password);
  }

  public boolean validatePhoneNumber(String PhoneNumber) throws PhoneNumberException {
    return phoneNumberValidatorService.validatePhoneNumber(PhoneNumber);
  }
}
