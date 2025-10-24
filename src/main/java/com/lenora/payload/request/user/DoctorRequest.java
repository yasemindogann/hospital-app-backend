package com.lenora.payload.request.user;

import com.lenora.entity.enums.Specialization;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DoctorRequest {

    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 50, message = "Full name must be between 3 and 50 characters")
    private String fullName;

    @NotNull(message = "Specialization is required")
    private Specialization specialization;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must be exactly 10 digits (e.g. 5425420000)"
    )
    private String phone;

    @NotNull(message = "User ID is required")
    private Long userId; // ilişkili kullanıcı ID'si
}
