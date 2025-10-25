package com.lenora.service.user;

import com.lenora.entity.concretes.user.User;
import com.lenora.entity.enums.Role;
import com.lenora.exception.ConflictException;
import com.lenora.payload.mapper.user.UserMapper;
import com.lenora.payload.messages.ErrorMessages;
import com.lenora.payload.messages.SuccessMessages;
import com.lenora.payload.request.user.UserRequest;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.payload.response.user.UserResponse;
import com.lenora.repository.user.DoctorRepository;
import com.lenora.repository.user.UserRepository;
import com.lenora.service.helper.MethodHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
                .message(SuccessMessages.USER_CREATED_SUCCESSFULLY)
                .httpStatus(HttpStatus.CREATED) // 201
                .object(userMapper.userToUserResponse(savedUser))
                .build();
    }

    // !!! 2) getAllUserWithList (Aktif kullanıcıları listele)
    @Transactional(readOnly = true)
    public ResponseMessage<List<UserResponse>> getAllUserWithList() {

        List<UserResponse> userList = userRepository.findAllByActiveTrue()
                .stream()
                .map(userMapper::userToUserResponse)
                .collect(Collectors.toList());

        return ResponseMessage.<List<UserResponse>>builder()
                .message(SuccessMessages.USER_LISTED_SUCCESSFULLY)
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
    public ResponseMessage<UserResponse> deleteUser(Long id) {

        User user = methodHelper.getByIdUser(id);

        //Soft delete uygula (aktiflik false yapılır)
        methodHelper.deactivateEntity(user);

        // Eğer user bir doctor’a bağlıysa, onu da pasifleştir
        doctorRepository.findByUserIdAndActiveTrue(user.getId())
                .ifPresent(doctor -> methodHelper.deactivateEntity(doctor));

        UserResponse userResponse = userMapper.userToUserResponse(user);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_DELETED_SUCCESSFULLY)
                .httpStatus(HttpStatus.OK)
                .object(userResponse)
                .build();
    }


    // BU ŞEKİLDE USERLARIN DB'DEN SİLİNMESİ TEHLİKELİ OLABİLİR.
    // GELECEKTE BU VERİLER LAZIM OLACAKTIR.
    // BU YÜZDEN DELETE İŞLEMİNDE USER'I PASİF YAPMAK DAHA DOĞRU OLACAKTIR DİYE DÜŞÜNDÜM.
    // SOFT DELETE İŞLEMİ GELİŞTİRİLEBİLİR.

/*
     // !!! 5) deleteUserById (Kullanıcı silme)
    @Transactional
    public ResponseMessage<UserResponse> deleteUser(Long id) {
        User user = methodHelper.getByIdUser(id);
        // Aynı zamanda bir User olan Doctor silme de kontrol edilmeli.
        // Entity üzerinden silmek, JPA ilişkilerini (örneğin Doctor) doğru şekilde yönetir
        userRepository.delete(user);
        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_DELETED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(null)
                .build();
    }
 */
}
