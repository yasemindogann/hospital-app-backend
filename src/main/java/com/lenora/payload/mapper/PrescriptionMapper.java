package com.lenora.payload.mapper;

import com.lenora.entity.concretes.Examination;
import com.lenora.entity.concretes.Patient;
import com.lenora.entity.concretes.Prescription;
import com.lenora.payload.request.PrescriptionRequest;
import com.lenora.payload.response.PatientResponse;
import com.lenora.payload.response.PrescriptionResponse;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionMapper {

    // 🔹 Request -> Entity
    public Prescription prescriptionRequestToPrescription(PrescriptionRequest prescriptionRequest, Examination examination){
        return Prescription.builder()
                .examination(examination)
                .medicineName(prescriptionRequest.getMedicineName())
                .dosage(prescriptionRequest.getDosage())
                .description(prescriptionRequest.getDescription())
                .build();
    }

    // 🔹 Entity -> Response
    public PrescriptionResponse prescriptionToPrescriptionResponse (Prescription prescription){
        return PrescriptionResponse.builder()
                .id(prescription.getId())
                .examinationId(prescription.getExamination().getId())
                .doctorName(prescription.getExamination().getDoctor().getFullName())
                .patientName(prescription.getExamination().getPatient().getFullName())
                .medicineName(prescription.getMedicineName())
                .dosage(prescription.getDosage())
                .description(prescription.getDescription())
                .build();
    }

}
