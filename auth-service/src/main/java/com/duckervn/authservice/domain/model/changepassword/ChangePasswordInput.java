package com.duckervn.authservice.domain.model.changepassword;

import com.duckervn.authservice.validation.annotation.ValidEmail;
import lombok.Data;

@Data
public class ChangePasswordInput {
    @ValidEmail(message = "email must be a valid email")
    private String email;

    private String oldPassword;

    private String newPassword;
}
