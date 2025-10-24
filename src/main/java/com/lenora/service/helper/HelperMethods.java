package com.lenora.service.helper;

import com.lenora.entity.concretes.Doctor;
import com.lenora.entity.concretes.User;
import com.lenora.exception.ResourceNotFoundException;
import com.lenora.payload.messages.ErrorMessages;
import com.lenora.payload.request.UserRequest;
import com.lenora.repository.DoctorRepository;
import com.lenora.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelperMethods {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    // userName ile unique kontrolü.Bu userName ile kullanıcı var mı?
    public boolean checkUserNameExists(String userName){
        return userRepository.existsByUserNameIgnoreCase(userName);
    }

    //User için -> getById için ayrı bir method oluşturduk.Update delete için de kullanılır.
    public User getByIdUser(Long id){
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND, id)));
    }

    //updateUser için userRequest'ten verileri al
    public void updateUserFromRequest(UserRequest userRequest, User user) {
        user.setUserName(userRequest.getUserName());
        user.setEmail(userRequest.getEmail());
        user.setRole(userRequest.getRole());
        user.setActive(userRequest.getActive());
    }

    //Doktor için -> getById için ayrı bir method oluşturduk.Update delete için de kullanılır.
    public Doctor getByIdDoctor(Long id){
        return doctorRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.DOCTOR_NOT_FOUND, id)));
    }


}
