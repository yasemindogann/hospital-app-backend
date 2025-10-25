package com.lenora.controller.user;

import com.lenora.payload.request.user.UserRequest;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.payload.response.user.UserResponse;
import com.lenora.service.user.UserService;
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

    // !!! 2) getAllUserWithList (Tüm kullanıcıları getir)
    @GetMapping
    public ResponseEntity<ResponseMessage<List<UserResponse>>> getAllUserWithList(){
        return ResponseEntity.ok(userService.getAllUserWithList());
    }

    // !!! 3) getUserById (İstenilen id'li kullanıcıyı getir)
    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage<UserResponse>> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // !!! 4) updateUserById (Kullanıcı güncelleme)
    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage<UserResponse>> updateUserById(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.updateUserById(id, userRequest));
    }

/*
    // !!! 5) deleteUserById (Kullanıcı silme)
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage<UserResponse>> deleteUser(@PathVariable Long id){
        return ResponseEntity.ok(userService.deleteUser(id));
    }
*/

}
