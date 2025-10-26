package com.lenora.controller.business;

import com.lenora.payload.request.business.PrescriptionRequest;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.payload.response.business.PrescriptionResponse;
import com.lenora.service.business.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    // !!! 1) savePrescription (Yeni reçete oluşturma)
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<ResponseMessage<PrescriptionResponse>> savePrescription(@Valid @RequestBody PrescriptionRequest prescriptionRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(prescriptionService.savePrescription(prescriptionRequest));
    }

    // !!! 2) getAllPrescriptionWithPageable (Bütün reçeteleri Pageable yapıda getir)
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','SECRETARY')")
    public ResponseEntity<ResponseMessage<Page<PrescriptionResponse>>> getAllPrescriptionWithPageable(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "type", defaultValue = "asc") String type
    ){
        return ResponseEntity.ok(prescriptionService.getAllPrescriptionWithPageable(page,size,sort,type));
    }

    // !!! 3) getPrescriptionById (İstenilen id'li reçeteyi getir)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','SECRETARY')")
    public ResponseEntity<ResponseMessage<PrescriptionResponse>> getPrescriptionById(@PathVariable Long id){
        return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
    }

    // !!! 4) updatePrescriptionById (Reçete güncelleme)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<ResponseMessage<PrescriptionResponse>> updatePrescriptionById(@PathVariable Long id, @Valid @RequestBody PrescriptionRequest prescriptionRequest){
        return ResponseEntity.ok(prescriptionService.updatePrescriptionById(id, prescriptionRequest));
    }

    // !!! 5) deletePrescription (Reçete silme)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<ResponseMessage<PrescriptionResponse>> deletePrescription(@PathVariable Long id){
        return ResponseEntity.ok(prescriptionService.deletePrescription(id));
    }

}
