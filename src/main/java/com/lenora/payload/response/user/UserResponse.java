package com.lenora.payload.response.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lenora.entity.enums.Role;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserResponse {

    private Long id;
    private String userName;
    private String email;
    private Role role;
    private Boolean active;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDateTime;
}
