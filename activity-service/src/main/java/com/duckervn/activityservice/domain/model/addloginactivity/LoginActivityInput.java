package com.duckervn.activityservice.domain.model.addloginactivity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginActivityInput {
    private String userId;

    private String description;
}
