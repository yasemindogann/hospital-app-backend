package com.lenora.controller;

import com.lenora.payload.request.ChangePasswordRequest;
import com.lenora.payload.request.LoginRequest;
import com.lenora.payload.response.AuthResponse;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage<AuthResponse>> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseMessage<Void>> logout() {
        return ResponseEntity.ok(authService.logout());
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseMessage<AuthResponse>> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseMessage<AuthResponse>> getCurrentUser() {
        return ResponseEntity.ok(authService.getCurrentUser());
    }

    @PutMapping("/change-password")
    public ResponseEntity<ResponseMessage<Void>> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        return ResponseEntity.ok(authService.changePassword(request));
    }
}
