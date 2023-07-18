package com.duckervn.authservice.domain.model.resetpassword;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ResetPasswordInput {
    @NotBlank(message = "password must not be blank")
    @Size(min = 8, max = 25, message = "password must be 8 - 25 character length")
    private String newPassword;
}
