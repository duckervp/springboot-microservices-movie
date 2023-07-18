package com.duckervn.authservice.domain.model.changepassword;

import com.duckervn.authservice.validation.annotation.ValidEmail;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ChangePasswordInput {
    @ValidEmail(message = "email must be a valid email")
    private String email;

    private String oldPassword;

    @NotBlank(message = "password must not be blank")
    @Size(min = 8, max = 25, message = "password must be 8 - 25 character length")
    private String newPassword;
}
