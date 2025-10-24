package com.lenora.service;

import com.lenora.entity.concretes.Doctor;
import com.lenora.entity.concretes.User;
import com.lenora.exception.ConflictException;
import com.lenora.exception.ResourceNotFoundException;
import com.lenora.payload.mapper.DoctorMapper;
import com.lenora.payload.messages.ErrorMessages;
import com.lenora.payload.messages.SuccessMessages;
import com.lenora.payload.request.DoctorRequest;
import com.lenora.payload.response.DoctorResponse;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.payload.response.UserResponse;
import com.lenora.repository.DoctorRepository;
import com.lenora.repository.UserRepository;
import com.lenora.service.helper.HelperMethods;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final UserService userService;
    private final HelperMethods helperMethods;

    // !!! 1) saveDoctor (Yeni doktor oluşturma)
    public ResponseMessage<DoctorResponse> saveDoctor(DoctorRequest doctorRequest){

        User user = userService.getUserByIdEntity(doctorRequest.getUserId());

        // User'ın rolünü kontrol et
        if(!user.getRole().name().equalsIgnoreCase("DOCTOR")){
            throw new ConflictException(String.format(ErrorMessages.USER_ROLE_NOT_DOCTOR , user.getId()));
        }

        // Bu user zaten bir doktorsa, ikinci kez eklenmesin
        if(doctorRepository.existsByUserId(user.getId())){
            throw new ConflictException(String.format(ErrorMessages.USER_ALREADY_REGISTERED_AS_DOCTOR, user.getId()));
        }

        Doctor doctor = doctorMapper.doctorRequestToDoctor(doctorRequest, user);

        Doctor savedDoctor = doctorRepository.save(doctor);

        return ResponseMessage.<DoctorResponse>builder()
                .message(SuccessMessages.DOCTOR_CREATED_SUCCESSFULY)
                .httpStatus(HttpStatus.CREATED)
                .object(doctorMapper.doctorToDoctorResponse(savedDoctor))
                .build();
    }

    // !!! 2) getAllDoctors (Bütün doktorları getir)
    public ResponseMessage<List<DoctorResponse>> getAllDoctorsWithList(){

        List<Doctor> doctors = doctorRepository.findAll();

        List<DoctorResponse> doctorList = doctors.stream()
                .map(doctorMapper::doctorToDoctorResponse)
                .toList();

        return ResponseMessage.<List<DoctorResponse>>builder()
                .message(SuccessMessages.DOCTOR_LISTED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(doctorList)
                .build();
    }

    // !!! 3) getDoctorById (İstenilen id'li doktoru getir)
    public ResponseMessage<DoctorResponse> getDoctorById(Long id){
        Doctor doctor = helperMethods.getByIdDoctor(id);

        return ResponseMessage.<DoctorResponse>builder()
                .message(SuccessMessages.DOCTOR_FOUNDED_SUCCESSFULY)
                .httpStatus(HttpStatus.OK)
                .object(doctorMapper.doctorToDoctorResponse(doctor))
                .build();
    }

    // !!! 4) updateDoctorById (Doktor güncelleme)
    public ResponseMessage<DoctorResponse> updateDoctorById(Long id, DoctorRequest doctorRequest) {
        Doctor doctor = helperMethods.getByIdDoctor(id);

        if( ){

        }

    }


}
