package com.lenora.controller;

import com.lenora.payload.request.ExaminationRequest;
import com.lenora.payload.response.ExaminationResponse;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.service.ExaminationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/examinations")
@RequiredArgsConstructor
public class ExaminationController {

    private final ExaminationService examinationService;

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @PostMapping
    public ResponseEntity<ResponseMessage<ExaminationResponse>> saveExamination(@RequestBody @Valid ExaminationRequest request) {
        return ResponseEntity.ok(examinationService.saveExamination(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage<ExaminationResponse>> updateExamination(@PathVariable Long id, @RequestBody @Valid ExaminationRequest request) {
        return ResponseEntity.ok(examinationService.updateExamination(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage<ExaminationResponse>> getExaminationById(@PathVariable Long id) {
        return ResponseEntity.ok(examinationService.getExaminationById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @GetMapping
    public ResponseEntity<ResponseMessage<List<ExaminationResponse>>> getAllExaminations() {
        return ResponseEntity.ok(examinationService.getAllExaminations());
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage<ExaminationResponse>> deleteExamination(@PathVariable Long id) {
        return ResponseEntity.ok(examinationService.deleteExamination(id));
    }
}
