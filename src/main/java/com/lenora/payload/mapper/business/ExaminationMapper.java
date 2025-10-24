package com.lenora.payload.mapper.business;

import com.lenora.entity.concretes.user.Doctor;
import com.lenora.entity.concretes.business.Examination;
import com.lenora.entity.concretes.user.Patient;
import com.lenora.payload.request.business.ExaminationRequest;
import com.lenora.payload.response.business.ExaminationResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ExaminationMapper {

    // 🔹 Request -> Entity
    public Examination examinationRequestToExamination(ExaminationRequest examinationRequest, Doctor doctor, Patient patient){
        return Examination.builder()
                .doctor(doctor)
                .patient(patient)
                .examinationDateTime(LocalDateTime.now())
                .examinationDiagnosis(examinationRequest.getExaminationDiagnosis())
                .build();
    }

    // 🔹 Entity -> Response
    public ExaminationResponse examinationToExaminationResponse(Examination examination){
        return ExaminationResponse.builder()
                .id(examination.getId())
                .doctorName(examination.getDoctor().getFullName())
                .patientName(examination.getPatient().getFullName())
                .examinationDateTime(examination.getExaminationDateTime())
                .examinationDiagnosis(examination.getExaminationDiagnosis())
                .build();
    }

}
