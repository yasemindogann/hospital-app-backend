package com.lenora.payload.mapper;

import com.lenora.entity.concretes.User;
import com.lenora.payload.request.UserRequest;
import com.lenora.payload.response.UserResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    // 🔹 Request -> Entity
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


    // 🔹 Entity -> Response
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

}
