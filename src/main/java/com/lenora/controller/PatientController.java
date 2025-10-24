package com.lenora.controller;

import com.lenora.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    // !!! 1) savePatient (Yeni hasta oluşturma)


}
