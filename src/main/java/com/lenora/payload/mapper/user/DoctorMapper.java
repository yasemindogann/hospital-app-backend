package com.lenora.payload.mapper.user;

import com.lenora.entity.concretes.user.Doctor;
import com.lenora.entity.concretes.user.User;
import com.lenora.payload.request.user.DoctorRequest;
import com.lenora.payload.response.user.DoctorResponse;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    // Request -> Entity
    public Doctor doctorRequestToDoctor(DoctorRequest doctorRequest, User user){
        return Doctor.builder()
                .fullName(doctorRequest.getFullName())
                .specialization(doctorRequest.getSpecialization())
                .phone(doctorRequest.getPhone())
                .user(user)
                .active(true)
                .build();
    }

    // Entity -> Response
    public DoctorResponse doctorToDoctorResponse(Doctor doctor){
        return DoctorResponse.builder()
                .id(doctor.getId())
                .fullName(doctor.getFullName())
                .specialization(doctor.getSpecialization())
                .phone(doctor.getPhone())
                .userId(doctor.getUser().getId())
                .active(doctor.getActive())
                .build();
    }

    // Update metodunda requestten yeni veriler alındığında DB'ye setlemek için
    public void updateDoctorFromRequest(DoctorRequest doctorRequest, Doctor doctor, User user) {
        doctor.setFullName(doctorRequest.getFullName());
        doctor.setSpecialization(doctorRequest.getSpecialization());
        doctor.setPhone(doctorRequest.getPhone());
        doctor.setUser(user);
    }

}
