package com.lenora.controller;

import com.lenora.payload.request.UserRequest;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.payload.response.UserResponse;
import com.lenora.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    // !!! 1) saveUser (Yeni kullanıcı oluşturma)
    @PostMapping
    public ResponseEntity<ResponseMessage<UserResponse>> saveUser(@Valid @RequestBody UserRequest userRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.saveUser(userRequest));
    }

    // !!! 2) getAllUser (Tüm kullanıcıları getir)
    @GetMapping
    public ResponseEntity<ResponseMessage<List<UserResponse>>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // !!! 3) getById (İstenilen id'li kullanıcıyı getir)
    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage<UserResponse>> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // !!! 4) updateUser (Kullanıcı güncelleme)
    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage<UserResponse>> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.updateUser(id, userRequest));
    }

    // !!! 5) deleteUser (Kullanıcı silme)
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage<UserResponse>> deleteUser(@PathVariable Long id){
        return ResponseEntity.ok(userService.deleteUser(id));
    }


}
