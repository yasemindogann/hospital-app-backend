package com.lenora.payload.messages;

public class ErrorMessages {
    private ErrorMessages() {}

    public static final String USER_ALREADY_EXISTS = "User already exists with username: %s";
    public static final String USER_NOT_FOUND = "User not found with id: %s";
    public static final String DOCTOR_NOT_FOUND = "Doctor not found with id: %s";
    public static final String USER_ROLE_NOT_DOCTOR  = "User with ID %d does not have DOCTOR role. Only users with DOCTOR role can be registered as doctors.";
    public static final String CANNOT_CHANGE_ROLE_OF_DOCTOR = "User with ID %d is a registered doctor and cannot change role.";
    public static final String USER_ALREADY_REGISTERED_AS_DOCTOR = "User with ID %d is already registered as a doctor.";

    public static final String PATIENT_NOT_FOUND = "Patient not found with id: %s";

    public static final String EXAMINATION_ALREADY_EXISTS = "Examination already exists";
    public static final String EXAMINATION_NOT_FOUND = "Examination not found with id: %s";

    public static final String PRESCRIPTION_NOT_FOUND = "Prescription not found with id: %s";
    public static final String PRESCRIPTION_ALREADY_EXISTS_FOR_EXAMINATION =
            "There is already a prescription registered for the examination with ID: %s";

}
