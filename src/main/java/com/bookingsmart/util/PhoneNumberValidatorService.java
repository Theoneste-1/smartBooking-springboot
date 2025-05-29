package com.bookingsmart.util;


import com.bookingsmart.exceptions.custom.PhoneNumberException;
import com.bookingsmart.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhoneNumberValidatorService {

    private final UserRepository userRepository;

    @Autowired
    public PhoneNumberValidatorService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean validatePhoneNumber(String phoneNumber) throws PhoneNumberException {

            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                throw new PhoneNumberException("Phone number cannot be null or empty");
            }

            phoneNumber = phoneNumber.trim();

            String localRegex = "^(07[2378])[0-9]{7}$";
            String internationalRegex = "^\\+250(7[2378])[0-9]{7}$";

            boolean isValidFormat = phoneNumber.matches(localRegex) || phoneNumber.matches(internationalRegex);

            if (!isValidFormat) {
                throw new PhoneNumberException(
                        "Phone number must start with 072, 078, 079, 073 or +250 followed by 78, 72, 79, 73, and have exactly 10 digits (local) or 12 digits (international)");
            }

            if (userRepository.existsByPhoneNumber(phoneNumber)) {
                throw new PhoneNumberException("Phone number already exists in the database");
            }
        return true;
    }
}