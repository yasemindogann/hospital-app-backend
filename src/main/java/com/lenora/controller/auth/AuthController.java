package com.lenora.controller.auth;

import com.lenora.entity.concretes.user.User;
import com.lenora.payload.request.login.ChangePasswordRequest;
import com.lenora.payload.request.login.LoginRequest;
import com.lenora.payload.response.login.LoginResponse;
import com.lenora.repository.user.UserRepository;
import com.lenora.security.jwt.JwtUtil;
import com.lenora.security.jwt.TokenBlacklist;
import com.lenora.security.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final TokenBlacklist tokenBlacklist;
    private final PasswordEncoder passwordEncoder;

    // ✅ JSON body ile login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
            );

            User user = userRepository.findByUserName(request.getUserName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String accessToken = jwtUtil.generateAccessToken(user.getUserName(), user.getRole().name());
            String refreshToken = jwtUtil.generateRefreshToken(user.getUserName());

            // ✅ HttpOnly cookie olarak token saklama
            ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                    .httpOnly(true)
                    .secure(false) // production'da true yap
                    .path("/")
                    .maxAge(3600) // 1 saat
                    .build();

            ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60) // 7 gün
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessCookie.toString(), refreshCookie.toString())
                    .body(new LoginResponse(accessToken, refreshToken, user.getUserName(), user.getRole().name()));

        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Kullanıcı adı veya şifre hatalı"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Beklenmeyen bir hata oluştu"));
        }
    }

    // ✅ Logout → Token blacklist'e eklenecek + cookie temizlenecek
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(value = "access_token", required = false) String accessToken) {
        if (accessToken != null) {
            tokenBlacklist.blacklist(accessToken);
        }

        ResponseCookie deleteAccess = ResponseCookie.from("access_token", "")
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie deleteRefresh = ResponseCookie.from("refresh_token", "")
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteAccess.toString(), deleteRefresh.toString())
                .body("Logged out successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findByUserName(userPrincipal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok()
                .body(Map.of(
                        "username", user.getUserName(),
                        "email", user.getEmail(),
                        "role", user.getRole(),
                        "active", user.getActive(),
                        "createdDateTime", user.getCreatedDateTime()
                ));
    }

    // ✅ Şifre değiştirme
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        User user = userRepository.findByUserName(request.getUserName()).orElseThrow();

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully");
    }

    // 🔹 Refresh Token endpoint
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(value = "refresh_token", required = false) String refreshToken) {
        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        String username = jwtUtil.extractUsername(refreshToken);
        String role = jwtUtil.extractRole(refreshToken);

        String newAccessToken = jwtUtil.generateAccessToken(username, role);

        ResponseCookie accessCookie = ResponseCookie.from("access_token", newAccessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(3600)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .body("Access token refreshed successfully");
    }
}
