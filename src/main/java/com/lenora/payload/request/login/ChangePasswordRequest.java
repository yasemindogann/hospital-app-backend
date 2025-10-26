package com.lenora.payload.request.login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    private String userName;
    private String oldPassword;
    private String newPassword;
}
