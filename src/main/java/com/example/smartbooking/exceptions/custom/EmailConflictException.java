package com.example.smartbooking.exceptions.custom;

public class EmailConflictException extends Exception {
    public EmailConflictException(String email) {
        super(email);
    }
}
