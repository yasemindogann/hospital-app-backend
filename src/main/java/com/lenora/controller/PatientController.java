package com.lenora.controller;

import com.lenora.payload.request.PatientRequest;
import com.lenora.payload.response.PatientResponse;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ResponseMessage<PatientResponse>> savePatient(@RequestBody @Valid PatientRequest request) {
        return ResponseEntity.ok(patientService.savePatient(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage<PatientResponse>> updatePatient(@PathVariable Long id, @RequestBody @Valid PatientRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage<PatientResponse>> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @GetMapping
    public ResponseEntity<ResponseMessage<List<PatientResponse>>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage<PatientResponse>> deletePatient(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.deletePatient(id));
    }
}
