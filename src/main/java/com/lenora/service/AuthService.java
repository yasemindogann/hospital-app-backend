package com.lenora.service;

import com.lenora.entity.concretes.User;
import com.lenora.payload.messages.ErrorMessages;
import com.lenora.payload.messages.SuccessMessages;
import com.lenora.payload.request.ChangePasswordRequest;
import com.lenora.payload.request.LoginRequest;
import com.lenora.payload.response.AuthResponse;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.repository.UserRepository;
import com.lenora.security.JwtTokenProvider;
import com.lenora.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseMessage<AuthResponse> login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .username(userDetails.getUsername())
                .role(userDetails.getRole())
                .build();

        return ResponseMessage.<AuthResponse>builder()
                .message(SuccessMessages.USER_FOUNDED)
                .httpStatus(HttpStatus.OK)
                .object(authResponse)
                .build();
    }

    public ResponseMessage<Void> logout() {
        return ResponseMessage.<Void>builder()
                .message("Logged out successfully")
                .httpStatus(HttpStatus.OK)
                .object(null)
                .build();
    }

    public ResponseMessage<AuthResponse> refreshToken(String refreshToken) {
        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }

        if (!jwtTokenProvider.validateJwtToken(refreshToken)) {
            throw new BadCredentialsException(ErrorMessages.INVALID_CREDENTIALS);
        }

        String username = jwtTokenProvider.getUserNameFromJwtToken(refreshToken);
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new BadCredentialsException(ErrorMessages.USER_NOT_FOUND);
        }

        String newToken = jwtTokenProvider.generateTokenFromUsername(username);
        AuthResponse authResponse = AuthResponse.builder()
                .token(newToken)
                .username(username)
                .role(userOpt.get().getRole().name())
                .build();

        return ResponseMessage.<AuthResponse>builder()
                .message("Token refreshed")
                .httpStatus(HttpStatus.OK)
                .object(authResponse)
                .build();
    }

    public ResponseMessage<AuthResponse> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new BadCredentialsException(ErrorMessages.ACCESS_DENIED);
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        AuthResponse authResponse = AuthResponse.builder()
                .username(userDetails.getUsername())
                .role(userDetails.getRole())
                .build();

        return ResponseMessage.<AuthResponse>builder()
                .message("Current user fetched")
                .httpStatus(HttpStatus.OK)
                .object(authResponse)
                .build();
    }

    public ResponseMessage<Void> changePassword(ChangePasswordRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new BadCredentialsException(ErrorMessages.ACCESS_DENIED);
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new BadCredentialsException(ErrorMessages.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseMessage.<Void>builder()
                .message(SuccessMessages.PASSWORD_CHANGED_RESPONSE_MESSAGE)
                .httpStatus(HttpStatus.OK)
                .object(null)
                .build();
    }
}
