package com.lenora.payload.mapper.user;

import com.lenora.entity.concretes.user.User;
import com.lenora.payload.request.user.UserRequest;
import com.lenora.payload.request.user.UserUpdateRequest;
import com.lenora.payload.response.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    // Request -> Entity
    public User userRequestToUser(UserRequest userRequest){
        return User.builder()
                .userName(userRequest.getUserName())
                .password(passwordEncoder.encode(userRequest.getPassword())) // Hashlenmiş şifre
                .email(userRequest.getEmail())
                .role(userRequest.getRole())
                .active(true)
                .createdDateTime(LocalDateTime.now())
                .build();
    }

    // Entity -> Response
    public UserResponse userToUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.getActive())
                .createdDateTime(user.getCreatedDateTime())
                .build();
    }

    // Entity -> Request mapping (Update için)
    public void updateUserFromRequest(UserUpdateRequest userUpdateRequest, User user) {
        user.setUserName(userUpdateRequest.getUserName());
        user.setEmail(userUpdateRequest.getEmail());
        user.setRole(userUpdateRequest.getRole());
    }
}
