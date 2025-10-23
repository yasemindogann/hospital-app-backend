package com.lenora.controller;

import com.lenora.payload.request.PrescriptionRequest;
import com.lenora.payload.response.PrescriptionResponse;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @PostMapping
    public ResponseEntity<ResponseMessage<PrescriptionResponse>> savePrescription(@RequestBody @Valid PrescriptionRequest request) {
        return ResponseEntity.ok(prescriptionService.savePrescription(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage<PrescriptionResponse>> updatePrescription(@PathVariable Long id, @RequestBody @Valid PrescriptionRequest request) {
        return ResponseEntity.ok(prescriptionService.updatePrescription(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage<PrescriptionResponse>> getPrescriptionById(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @GetMapping
    public ResponseEntity<ResponseMessage<List<PrescriptionResponse>>> getAllPrescriptions() {
        return ResponseEntity.ok(prescriptionService.getAllPrescriptions());
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage<PrescriptionResponse>> deletePrescription(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.deletePrescription(id));
    }
}
