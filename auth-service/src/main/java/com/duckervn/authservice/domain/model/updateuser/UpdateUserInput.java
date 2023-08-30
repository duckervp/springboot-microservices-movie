package com.duckervn.authservice.domain.model.updateuser;

import com.duckervn.authservice.validation.annotation.NotBlankIfNotNull;
import com.duckervn.authservice.validation.annotation.ValidEmail;
import com.duckervn.authservice.validation.annotation.ValidGender;
import com.duckervn.authservice.validation.annotation.ValidPhoneNumber;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;

@Data
public class UpdateUserInput {

    @Column(unique = true)
//    @ValidEmail(message = "email must be a valid email")
    private String email;

    @NotBlankIfNotNull(message = "name must not be blank")
    @Size(min = 3, max = 25, message = "name must be 3 - 25 character length")
    private String name;

    @Enumerated(EnumType.STRING)
    @ValidGender(message = "gender must be valid gender")
    private String gender;

    @ValidPhoneNumber(message = "phone number must be a string of 10 or 11 digits")
    private String phoneNumber;

    @NotBlankIfNotNull(message = "address must not be blank")
    private String address;

    @NotBlankIfNotNull(message = "birthdate must not be blank")
    private String birthdate;

    private Integer status;

    private String avatarUrl;
}
