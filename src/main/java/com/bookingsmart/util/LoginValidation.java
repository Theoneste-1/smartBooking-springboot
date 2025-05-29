package com.bookingsmart.util;

import com.bookingsmart.models.User;
import com.bookingsmart.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginValidation {
    @Autowired
    private UserRepository userRepository;

    public User isUsernameExists(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()) {
            return user.get();
        }
        else {
            throw new UsernameNotFoundException("Username not found");
        }

    }
}
