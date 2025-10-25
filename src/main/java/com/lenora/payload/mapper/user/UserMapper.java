package com.lenora.payload.mapper.user;

import com.lenora.entity.concretes.user.User;
import com.lenora.payload.request.user.UserRequest;
import com.lenora.payload.response.user.UserResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    // Request -> Entity
    public User userRequestToUser(UserRequest userRequest){
        return User.builder()
                .userName(userRequest.getUserName())
                .password(userRequest.getPassword())
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
    public void updateUserFromRequest(UserRequest userRequest, User user) {
        user.setUserName(userRequest.getUserName());
        user.setEmail(userRequest.getEmail());
        user.setRole(userRequest.getRole());
//      user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        // Password değiştirilirse burada kontrol edilecek
        // if (userRequest.getPassword() != null && !userRequest.getPassword().isBlank()) {
        //     user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        // }
    }
}
