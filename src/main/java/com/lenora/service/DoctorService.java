package com.lenora.service;

import com.lenora.entity.concretes.Doctor;
import com.lenora.entity.concretes.User;
import com.lenora.payload.mapper.DoctorMapper;
import com.lenora.payload.request.DoctorRequest;
import com.lenora.payload.response.DoctorResponse;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.payload.messages.SuccessMessages;
import com.lenora.payload.messages.ErrorMessages;
import com.lenora.exception.ResourceNotFoundException;
import com.lenora.repository.DoctorRepository;
import com.lenora.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    public ResponseMessage<DoctorResponse> saveDoctor(DoctorRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND));

        Doctor doctor = DoctorMapper.mapRequestToDoctor(request, user);
        doctorRepository.save(doctor);

        return ResponseMessage.<DoctorResponse>builder()
                .message(SuccessMessages.DOCTOR_CREATED)
                .httpStatus(HttpStatus.CREATED)
                .object(DoctorMapper.mapDoctorToResponse(doctor))
                .build();
    }

    public ResponseMessage<DoctorResponse> updateDoctor(Long id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.DOCTOR_NOT_FOUND));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND));

        doctor.setFullName(request.getFullName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setPhone(request.getPhone());
        doctor.setUser(user);

        doctorRepository.save(doctor);

        return ResponseMessage.<DoctorResponse>builder()
                .message(SuccessMessages.DOCTOR_UPDATED)
                .httpStatus(HttpStatus.OK)
                .object(DoctorMapper.mapDoctorToResponse(doctor))
                .build();
    }

    public ResponseMessage<DoctorResponse> getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.DOCTOR_NOT_FOUND));

        return ResponseMessage.<DoctorResponse>builder()
                .message(SuccessMessages.DOCTOR_FOUNDED)
                .httpStatus(HttpStatus.OK)
                .object(DoctorMapper.mapDoctorToResponse(doctor))
                .build();
    }

    public ResponseMessage<List<DoctorResponse>> getAllDoctors() {
        List<DoctorResponse> doctors = doctorRepository.findAll()
                .stream()
                .map(DoctorMapper::mapDoctorToResponse)
                .collect(Collectors.toList());

        return ResponseMessage.<List<DoctorResponse>>builder()
                .message(SuccessMessages.DOCTOR_FOUNDED)
                .httpStatus(HttpStatus.OK)
                .object(doctors)
                .build();
    }

    public ResponseMessage<DoctorResponse> deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.DOCTOR_NOT_FOUND));

        doctorRepository.delete(doctor);

        return ResponseMessage.<DoctorResponse>builder()
                .message(SuccessMessages.DOCTOR_DELETED)
                .httpStatus(HttpStatus.OK)
                .object(DoctorMapper.mapDoctorToResponse(doctor))
                .build();
    }
}
