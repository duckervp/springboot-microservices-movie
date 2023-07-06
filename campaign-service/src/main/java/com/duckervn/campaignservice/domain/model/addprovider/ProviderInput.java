package com.duckervn.campaignservice.domain.model.addprovider;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProviderInput {
    private Long id;

    private String name;

    private String status;

    private String type;

    private String sendMethod;

    private String hostname;

    private Integer port;

    private String username;

    private String password;

    private String apiDomainName;

    private String apiKey;

    private String sender;
}
