package com.bookingsmart.exceptions.custom;

public class UsernameConflictException extends Exception {
    public UsernameConflictException(String message) {
        super(message);
    }
}
