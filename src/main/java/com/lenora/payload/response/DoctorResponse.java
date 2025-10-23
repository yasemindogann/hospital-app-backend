package com.lenora.payload.response;

import com.lenora.entity.enums.Specialization;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DoctorResponse {

    private Long id;
    private String fullName;
    private Specialization specialization;
    private String phone;
    private Long userId;
}
