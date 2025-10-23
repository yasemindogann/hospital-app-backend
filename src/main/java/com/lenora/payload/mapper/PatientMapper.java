package com.lenora.payload.mapper;

import com.lenora.entity.concretes.Patient;
import com.lenora.payload.request.PatientRequest;
import com.lenora.payload.response.PatientResponse;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    // 🔹 Request -> Entity
    public Patient patientRequestToPatient(PatientRequest patientRequest){
        return Patient.builder()
                .fullName(patientRequest.getFullName())
                .identityNumber(patientRequest.getIdentityNumber())
                .birthDate(patientRequest.getBirthDate())
                .phone(patientRequest.getPhone())
                .build();
    }


    // 🔹 Entity -> Response
    public PatientResponse patientToPatientResponse(Patient patient){
        return PatientResponse.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .identityNumber(patient.getIdentityNumber())
                .birthDate(patient.getBirthDate())
                .phone(patient.getPhone())
                .build();
    }

}
