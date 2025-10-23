package com.lenora.controller;

import com.lenora.payload.request.DoctorRequest;
import com.lenora.payload.response.DoctorResponse;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping // http://localhost:8080/api/doctors
    public ResponseEntity<ResponseMessage<DoctorResponse>> saveDoctor(@RequestBody @Valid DoctorRequest request) {
        return ResponseEntity.ok(doctorService.saveDoctor(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage<DoctorResponse>> updateDoctor(@PathVariable Long id, @RequestBody @Valid DoctorRequest request) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage<DoctorResponse>> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @GetMapping
    public ResponseEntity<ResponseMessage<List<DoctorResponse>>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage<DoctorResponse>> deleteDoctor(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.deleteDoctor(id));
    }
}
