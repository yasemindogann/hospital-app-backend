package com.lenora.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PatientResponse {

    private Long id;

    private String fullName;

    private String identityNumber;

    private LocalDate birthDate;

    private String phone;
}
