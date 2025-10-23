package com.lenora.service;

import com.lenora.entity.concretes.Patient;
import com.lenora.payload.mapper.PatientMapper;
import com.lenora.payload.request.PatientRequest;
import com.lenora.payload.response.PatientResponse;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.payload.messages.SuccessMessages;
import com.lenora.payload.messages.ErrorMessages;
import com.lenora.exception.ResourceNotFoundException;
import com.lenora.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public ResponseMessage<PatientResponse> savePatient(PatientRequest request) {
        Patient patient = PatientMapper.mapRequestToPatient(request);
        patientRepository.save(patient);

        return ResponseMessage.<PatientResponse>builder()
                .message(SuccessMessages.PATIENT_CREATED)
                .httpStatus(HttpStatus.CREATED)
                .object(PatientMapper.mapPatientToResponse(patient))
                .build();
    }

    public ResponseMessage<PatientResponse> updatePatient(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PATIENT_NOT_FOUND));

        patient.setFullName(request.getFullName());
        patient.setTcIdentityNumber(request.getTcIdentityNumber());
        patient.setBirthDate(request.getBirthDate());
        patient.setPhone(request.getPhone());

        patientRepository.save(patient);

        return ResponseMessage.<PatientResponse>builder()
                .message(SuccessMessages.PATIENT_UPDATED)
                .httpStatus(HttpStatus.OK)
                .object(PatientMapper.mapPatientToResponse(patient))
                .build();
    }

    public ResponseMessage<PatientResponse> getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PATIENT_NOT_FOUND));

        return ResponseMessage.<PatientResponse>builder()
                .message(SuccessMessages.PATIENT_FOUNDED)
                .httpStatus(HttpStatus.OK)
                .object(PatientMapper.mapPatientToResponse(patient))
                .build();
    }

    public ResponseMessage<List<PatientResponse>> getAllPatients() {
        List<PatientResponse> patients = patientRepository.findAll()
                .stream()
                .map(PatientMapper::mapPatientToResponse)
                .collect(Collectors.toList());

        return ResponseMessage.<List<PatientResponse>>builder()
                .message(SuccessMessages.PATIENT_FOUNDED)
                .httpStatus(HttpStatus.OK)
                .object(patients)
                .build();
    }

    public ResponseMessage<PatientResponse> deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PATIENT_NOT_FOUND));

        patientRepository.delete(patient);

        return ResponseMessage.<PatientResponse>builder()
                .message(SuccessMessages.PATIENT_DELETED)
                .httpStatus(HttpStatus.OK)
                .object(PatientMapper.mapPatientToResponse(patient))
                .build();
    }
}
