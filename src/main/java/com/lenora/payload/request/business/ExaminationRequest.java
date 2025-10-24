package com.lenora.payload.request.business;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ExaminationRequest {

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotBlank(message = "Examination diagnosis is required")
    @Size(min = 5, max = 255, message = "Diagnosis must be between 5 and 255 characters")
    private String examinationDiagnosis;
}
