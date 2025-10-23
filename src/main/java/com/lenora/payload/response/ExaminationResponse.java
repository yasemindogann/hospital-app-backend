package com.lenora.payload.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ExaminationResponse {

    private Long id;

    private String doctorName;

    private String patientName;

    private LocalDateTime examinationDateTime;

    private String examinationDiagnosis;
}
