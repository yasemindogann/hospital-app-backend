package com.lenora.controller.user;

import com.lenora.payload.request.user.PatientRequest;
import com.lenora.payload.response.user.PatientResponse;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.service.user.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    // !!! 1) savePatient (Yeni hasta oluşturma)
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARY')")
    public ResponseEntity<ResponseMessage<PatientResponse>> savePatient(@Valid @RequestBody PatientRequest patientRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(patientService.savePatient(patientRequest));
    }

    // !!! 2) getAllPatientWithPageable (Hastaları pageable yapıda getir)
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARY','DOCTOR')")
    public ResponseEntity<ResponseMessage<Page<PatientResponse>>> getAllPatientWithPageable(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort, //id'ye göre sırala
            @RequestParam(value = "type", defaultValue = "asc") String type
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(patientService.getAllPatientWithPageable(page,size,sort,type));
    }

    // !!! 3) getPatientById (İstenilen id'li hastayı getir)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARY','DOCTOR')")
    public ResponseEntity<ResponseMessage<PatientResponse>> getPatientById(@PathVariable Long id){
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    // !!! 4) updatePatientById (Hastaları güncelleme)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARY')")
    public ResponseEntity<ResponseMessage<PatientResponse>> updatePatientById(@PathVariable Long id, @Valid @RequestBody PatientRequest patientRequest) {
        return ResponseEntity.ok(patientService.updatePatientById(id, patientRequest));
    }

    // !!! 5) deletePatientById (id ile hasta silme)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseMessage<PatientResponse>> deletePatient(@PathVariable Long id){
        return ResponseEntity.ok(patientService.deletePatient(id));
    }

}
