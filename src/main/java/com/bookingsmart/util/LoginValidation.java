package com.bookingsmart.util;

import com.bookingsmart.exceptions.custom.EmailConflictException;
import com.bookingsmart.exceptions.custom.EmailValidationException;
import com.bookingsmart.models.User;
import com.bookingsmart.repositories.UserRepository;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LoginValidation {
    @Autowired
    private UserRepository userRepository;

    public boolean validateEmail(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public User isUsernameExists(String username) throws EmailValidationException {
        // validate email
        boolean isEmailValid = this.validateEmail(username);
        if(userRepository.existsByUsername(username)) {
            return userRepository.findByUsername(username).get();
        }else if(isEmailValid) {  // if the user doesn't exist by the username means exists by his email,
            if(userRepository.existsByEmail(username)) {
                return userRepository.findByEmail(username).get();
            }else {
                throw new UsernameNotFoundException( "User with email = " + username + " not found");
            }
        }else {
            throw new EmailValidationException("Invalid Email or username");
        }
    }
}
