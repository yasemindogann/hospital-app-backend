package com.lenora.payload.messages;

public class ErrorMessages {
    private ErrorMessages() {}

    public static final String USER_ALREADY_EXISTS = "User already exists with username: %s";
    public static final String USER_NOT_FOUND = "User not found with id: %s";
    public static final String DOCTOR_NOT_FOUND = "Doctor not found with id: %s";

}
