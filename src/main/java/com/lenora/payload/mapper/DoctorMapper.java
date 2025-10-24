package com.lenora.payload.mapper;

import com.lenora.entity.concretes.Doctor;
import com.lenora.entity.concretes.User;
import com.lenora.payload.request.DoctorRequest;
import com.lenora.payload.response.DoctorResponse;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    // 🔹 Request -> Entity
    public Doctor doctorRequestToDoctor(DoctorRequest doctorRequest, User user){
        return Doctor.builder()
                .fullName(doctorRequest.getFullName())
                .specialization(doctorRequest.getSpecialization())
                .phone(doctorRequest.getPhone())
                .user(user)
                .build();
    }

    // 🔹 Entity -> Response
    public DoctorResponse doctorToDoctorResponse(Doctor doctor){
        return DoctorResponse.builder()
                .id(doctor.getId())
                .fullName(doctor.getFullName())
                .specialization(doctor.getSpecialization())
                .phone(doctor.getPhone())
                .userId(doctor.getUser().getId())
                .build();
    }

    // Doctor update işlemi için DoctorRequest → Doctor mapping
    public void updateDoctorFromRequest(DoctorRequest doctorRequest, Doctor doctor, User user) {
        doctor.setFullName(doctorRequest.getFullName());
        doctor.setSpecialization(doctorRequest.getSpecialization());
        doctor.setPhone(doctorRequest.getPhone());
        doctor.setUser(user);
    }

}
