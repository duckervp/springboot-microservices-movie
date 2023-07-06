package com.duckervn.authservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reset_password_token")
public class ResetPasswordToken {
    @Id
    private String id;
    private String clientId;
    private String token;
    private Long expiredAt;
}
