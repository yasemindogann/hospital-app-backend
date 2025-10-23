package com.lenora.service;

import com.lenora.entity.concretes.User;
import com.lenora.payload.mapper.UserMapper;
import com.lenora.payload.request.UserRequest;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.payload.response.UserResponse;
import com.lenora.payload.messages.ErrorMessages;
import com.lenora.payload.messages.SuccessMessages;
import com.lenora.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseMessage<UserResponse> saveUser(UserRequest request) {
        User user = UserMapper.mapRequestToUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_CREATED)
                .httpStatus(HttpStatus.CREATED)
                .object(UserMapper.mapUserToResponse(user))
                .build();
    }

    public ResponseMessage<UserResponse> updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.USER_NOT_FOUND));

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        userRepository.save(user);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_UPDATED)
                .httpStatus(HttpStatus.OK)
                .object(UserMapper.mapUserToResponse(user))
                .build();
    }

    public ResponseMessage<UserResponse> getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.USER_NOT_FOUND));

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_FOUNDED)
                .httpStatus(HttpStatus.OK)
                .object(UserMapper.mapUserToResponse(user))
                .build();
    }

    public ResponseMessage<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userRepository.findAll()
                .stream()
                .map(UserMapper::mapUserToResponse)
                .collect(Collectors.toList());

        return ResponseMessage.<List<UserResponse>>builder()
                .message(SuccessMessages.USER_FOUNDED)
                .httpStatus(HttpStatus.OK)
                .object(users)
                .build();
    }

    public ResponseMessage<UserResponse> deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.USER_NOT_FOUND));

        userRepository.delete(user);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_DELETED)
                .httpStatus(HttpStatus.OK)
                .object(UserMapper.mapUserToResponse(user))
                .build();
    }
}