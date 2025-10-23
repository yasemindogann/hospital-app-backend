package com.lenora.service;

import com.lenora.entity.concretes.User;
import com.lenora.exception.ConflictException;
import com.lenora.payload.mapper.UserMapper;
import com.lenora.payload.messages.ErrorMessages;
import com.lenora.payload.messages.SuccessMessages;
import com.lenora.payload.request.UserRequest;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.payload.response.UserResponse;
import com.lenora.repository.UserRepository;
import lombok.RequiredArgsConstructor;;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    // !!! 1) saveUser (Yeni kullanıcı oluşturma)
    public ResponseMessage<UserResponse> saveUser(UserRequest userRequest){
        //Bu username ile kayıtlı kullanıcı var mı kontrolü
        //Varsa bu kullanıcı adı ile kayıtlı user var diye mesaj verecek, yoksa bir şey yapmayacak
        checkUserNameExists(userRequest.getUserName());

        //userRequest'i User'a çevirdi
        User user = userMapper.userRequestToUser(userRequest);

        //User artık DB'ye kaydolmaya hazır.
        User savedUser = userRepository.save(user);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_CREATED_SUCCESSFULY) //Kaydetme başarılı mesajı
                .httpStatus(HttpStatus.CREATED) //CREATED http kodu -> 201
                .object(userMapper.userToUserResponse(savedUser)) //Kaydedilen UserResponse
                .build();
    }

    // userName ile unique kontrolü.Bu userName ile kullanıcı var mı?
    //Varsa bu username ile kayıtlı kullanıcı var diyecek yoksa bir şey yapmayacak.
    public void checkUserNameExists(String userName){
        if(userRepository.existsByUserNameIgnoreCase(userName))
            throw new ConflictException(String.format(ErrorMessages.USER_ALREADY_EXISTS, userName));
    }


}