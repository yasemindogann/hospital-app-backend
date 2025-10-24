package com.lenora.payload.response.business;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PrescriptionResponse {

    private Long id;

    private Long examinationId;

    private String doctorName;

    private String patientName;

    private String medicineName;

    private String dosage;

    private String description;
}
