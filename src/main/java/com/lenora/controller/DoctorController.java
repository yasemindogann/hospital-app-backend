package com.lenora.controller;

import com.lenora.entity.concretes.User;
import com.lenora.payload.request.DoctorRequest;
import com.lenora.payload.response.DoctorResponse;
import com.lenora.payload.response.ResponseMessage;
import com.lenora.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    // !!! 1) saveDoctor (Yeni kullanıcı oluşturma)
    @PostMapping
    public ResponseEntity<ResponseMessage<DoctorResponse>> saveDoctor(@Valid @RequestBody DoctorRequest doctorRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(doctorService.saveDoctor(doctorRequest));
    }

    // !!! 2) getAllDoctors (Bütün doktorları getir)
    @GetMapping
    public ResponseEntity<ResponseMessage<List<DoctorResponse>>> getAllDoctors(){
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    // !!! 3) getDoctorById (İstenilen id'li doktoru getir)
    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage<DoctorResponse>> getDoctorById(@PathVariable Long id){
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }



}
