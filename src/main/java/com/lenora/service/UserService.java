package com.lenora.service;

import com.lenora.entity.concretes.User;
import com.lenora.entity.enums.Role;
import com.lenora.exception.ConflictException;
import com.lenora.payload.mapper.UserMapper;
import com.lenora.payload.messages.ErrorMessages;
import com.lenora.payload.messages.SuccessMessages;
import com.lenora.payload.request.UserRequest;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.payload.response.UserResponse;
import com.lenora.repository.DoctorRepository;
import com.lenora.repository.UserRepository;
import com.lenora.service.helper.MethodHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MethodHelper methodHelper;
    private final DoctorRepository doctorRepository;

    // !!! 1) saveUser (Yeni kullanıcı oluşturma)
    @Transactional
    public ResponseMessage<UserResponse> saveUser(UserRequest userRequest) {
        if (methodHelper.checkUserNameExists(userRequest.getUserName())) {
            throw new ConflictException(
                    String.format(ErrorMessages.USER_ALREADY_EXISTS, userRequest.getUserName())
            );
        }

        User user = userMapper.userRequestToUser(userRequest);
        User savedUser = userRepository.save(user);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_CREATED_SUCCESSFULY)
                .httpStatus(HttpStatus.CREATED)
                .object(userMapper.userToUserResponse(savedUser))
                .build();
    }

    // !!! 2) getAllUserWithList (Tüm kullanıcıları getir)
    public ResponseMessage<List<UserResponse>> getAllUserWithList() {
        List<UserResponse> userList = userRepository.findAll()
                .stream()
                .map(userMapper::userToUserResponse)
                .toList();

        return ResponseMessage.<List<UserResponse>>builder()
                .message(SuccessMessages.USER_LISTED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(userList)
                .build();
    }

    // !!! 3) getUserById (İstenilen id'li kullanıcıyı getir)
    public ResponseMessage<UserResponse> getUserById(Long id) {
        User user = methodHelper.getByIdUser(id);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_FOUNDED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(userMapper.userToUserResponse(user))
                .build();
    }

    // !!! 4) updateUserById (Kullanıcı güncelleme)
    @Transactional
    public ResponseMessage<UserResponse> updateUserById(Long id, UserRequest userRequest) {
        User user = methodHelper.getByIdUser(id);

        // Username başka kullanıcıda varsa hata
        if (!user.getUserName().equalsIgnoreCase(userRequest.getUserName()) &&
                methodHelper.checkUserNameExists(userRequest.getUserName())) {
            throw new ConflictException(
                    String.format(ErrorMessages.USER_ALREADY_EXISTS, userRequest.getUserName())
            );
        }

        //Rolü DOCTOR olan bir User update ile ADMIN olamasın
        // Eğer bu kullanıcı zaten doktor tablosunda varsa (yani bir doktor kayıtlıysa)
        if (doctorRepository.existsByUserId(user.getId()) && userRequest.getRole() != Role.DOCTOR) {
            throw new ConflictException(String.format(ErrorMessages.CANNOT_CHANGE_ROLE_OF_DOCTOR, user.getId()));
        }

        userMapper.updateUserFromRequest(userRequest, user);
        User updatedUser = userRepository.save(user);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_UPDATED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(userMapper.userToUserResponse(updatedUser))
                .build();
    }

    // !!! 5) deleteUserById (Kullanıcı silme)
    @Transactional
    public ResponseMessage<UserResponse> deleteUser(Long id) {
        User user = methodHelper.getByIdUser(id);

        // Eğer user bir doktorsa, ona bağlı doktor kaydını sil
        doctorRepository.findByUserId(user.getId())
                .ifPresent(doctorRepository::delete); //ifPresent -> değer varsa sil, yoksa hiçbir şey yapma

        // Entity üzerinden silmek, JPA ilişkilerini (örneğin Doctor) doğru şekilde yönetir
        userRepository.delete(user);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_DELETED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(null)
                .build();
    }
}
