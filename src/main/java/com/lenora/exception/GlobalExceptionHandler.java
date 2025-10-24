package com.lenora.exception;

import com.lenora.payload.response.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 🔹 Validation (Bean Validation) hataları
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseMessage<Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ResponseMessage<Map<String, String>> response = ResponseMessage.<Map<String, String>>builder()
                .message("Validation error")
                .httpStatus(HttpStatus.BAD_REQUEST)
                .object(errors)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 🔹 Conflict hatası (örneğin username veya email çakışması)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ResponseMessage<Void>> handleConflict(ConflictException ex) {

        ResponseMessage<Void> response = ResponseMessage.<Void>builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.CONFLICT)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // 🔹 Resource bulunamadı hatası (örneğin id yok)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseMessage<Void>> handleNotFound(ResourceNotFoundException ex) {

        ResponseMessage<Void> response = ResponseMessage.<Void>builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // 🔹 Bad Request hatası
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseMessage<Void>> handleBadRequest(BadRequestException ex) {

        ResponseMessage<Void> response = ResponseMessage.<Void>builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 🔹 Diğer tüm beklenmedik hatalar
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessage<Void>> handleGeneral(Exception ex) {

        ResponseMessage<Void> response = ResponseMessage.<Void>builder()
                .message("An unexpected error occurred: " + ex.getMessage())
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ResponseMessage<Void>> handleMissingPathVariable(MissingPathVariableException ex) {
        ResponseMessage<Void> response = ResponseMessage.<Void>builder()
                .message("Missing required path variable: " + ex.getVariableName())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
