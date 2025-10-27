package com.lenora.payload.mapper.user;

import com.lenora.entity.concretes.user.Patient;
import com.lenora.payload.request.user.PatientRequest;
import com.lenora.payload.response.user.PatientResponse;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    // Request -> Entity
    public Patient patientRequestToPatient(PatientRequest patientRequest){
        return Patient.builder()
                .fullName(patientRequest.getFullName())
                .identityNumber(patientRequest.getIdentityNumber())
                .birthDate(patientRequest.getBirthDate())
                .phone(patientRequest.getPhone())
                .active(true)
                .build();
    }


    // Entity -> Response
    public PatientResponse patientToPatientResponse(Patient patient){
        return PatientResponse.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .identityNumber(patient.getIdentityNumber())
                .birthDate(patient.getBirthDate())
                .phone(patient.getPhone())
                .active(patient.getActive())
                .build();
    }

    // Update metodunda requestten yeni veriler alındığında DB'ye setlemek için
    public void updatePatientFromRequest(PatientRequest patientRequest, Patient patient){
        patient.setFullName(patientRequest.getFullName());
        patient.setIdentityNumber(patientRequest.getIdentityNumber());
        patient.setBirthDate(patientRequest.getBirthDate());
        patient.setPhone(patientRequest.getPhone());
    }

}
