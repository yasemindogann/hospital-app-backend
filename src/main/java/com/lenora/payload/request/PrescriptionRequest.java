package com.lenora.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PrescriptionRequest {

    @NotNull(message = "Examination ID is required")
    private Long examinationId;

    @NotBlank(message = "Medicine name is required")
    @Size(min = 2, max = 100, message = "Medicine name must be between 2 and 100 characters")
    private String medicineName;

    @NotBlank(message = "Dosage information is required")
    @Size(min = 1, max = 50, message = "Dosage must be between 1 and 50 characters")
    private String dosage;

    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;
}
