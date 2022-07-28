package com.duckervn.authservice.domain.entity;

import com.duckervn.authservice.validation.annotation.NotBlankIfNotNull;
import com.duckervn.authservice.validation.annotation.ValidEmail;
import com.duckervn.authservice.validation.annotation.ValidPhoneNumber;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    @Id
    private String id;

    @NotBlankIfNotNull(message = "name must not be blank")
    private String name;

    @Column(unique = true)
    @ValidEmail(message = "email must be a valid email")
    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ValidPhoneNumber(message = "phone number must be a string of 10 or 11 digits")
    private String phoneNumber;

    @NotBlankIfNotNull(message = "address must not be blank")
    private String address;

    private LocalDate dob;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
