package com.lenora.service;

import com.lenora.entity.concretes.User;
import com.lenora.exception.ConflictException;
import com.lenora.exception.ResourceNotFoundException;
import com.lenora.payload.mapper.UserMapper;
import com.lenora.payload.messages.ErrorMessages;
import com.lenora.payload.messages.SuccessMessages;
import com.lenora.payload.request.UserRequest;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.payload.response.UserResponse;
import com.lenora.repository.UserRepository;
import com.lenora.service.helper.HelperMethods;
import lombok.RequiredArgsConstructor;;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final HelperMethods helperMethods;


    // !!! 1) saveUser (Yeni kullanıcı oluşturma)
    @Transactional
    public ResponseMessage<UserResponse> saveUser(UserRequest userRequest){

        //Bu username ile kayıtlı kullanıcı var mı kontrolü
        if(helperMethods.checkUserNameExists(userRequest.getUserName())){
            throw new ConflictException(String.format(ErrorMessages.USER_ALREADY_EXISTS, userRequest.getUserName()));
        }

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

    // !!! 2) getAllUserWithList (Tüm kullanıcıları getir)
    public ResponseMessage<List<UserResponse>> getAllUserWithList(){
        List<User> users = userRepository.findAll();

        List<UserResponse> userList = users.stream()
                .map(userMapper::userToUserResponse)
                .toList();

        return ResponseMessage.<List<UserResponse>>builder()
                .message(SuccessMessages.USER_LISTED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(userList)
                .build();

    }

    // !!! 3) getById (İstenilen id'li kullanıcıyı getir)
    public ResponseMessage<UserResponse> getUserById(Long id){
        User user = helperMethods.getByIdUser(id);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_FOUNDED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(userMapper.userToUserResponse(user))
                .build();

    }

    // !!! 4) updateUser (Kullanıcı güncelleme)
    @Transactional //method çalışırken exception olursa DB değişiklikleri otomatik geri alınır (rollback)
    public ResponseMessage<UserResponse> updateUserById(Long id, UserRequest userRequest){
        //güncellenmek istenilen kullanıcı DB'de var mı
        User user = helperMethods.getByIdUser(id);

        //Username değişmişse ve başka bir kullanıcıda varsa hata fırlat
        if (!user.getUserName().equalsIgnoreCase(userRequest.getUserName()) &&
                helperMethods.checkUserNameExists(userRequest.getUserName())) {
            throw new ConflictException(String.format(ErrorMessages.USER_ALREADY_EXISTS, userRequest.getUserName()));
        }

//        // Password boş değilse veya null değilse encode et
//        if (userRequest.getPassword() != null && !userRequest.getPassword().isBlank()) {
//            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
//        }

        helperMethods.updateUserFromRequest(userRequest, user);

        User updatedUser = userRepository.save(user);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_UPDATED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(userMapper.userToUserResponse(updatedUser))
                .build();
    }

    // !!! 5) deleteUser (Kullanıcı silme)
    @Transactional
    public ResponseMessage<UserResponse> deleteUserById(Long id){
        helperMethods.getByIdUser(id);
        userRepository.deleteById(id);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_DELETED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(null)
                .build();
    }

    public User getUserByIdEntity(Long id){
        return userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND, id)));
    }

}