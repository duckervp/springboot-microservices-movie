package com.duckervn.campaignservice.domain.model.getcampaign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignOutput {
    private Long id;

    private String name;

    private String description;

    private String status;

    private String type;

    private ProviderOutput provider;

    private String subject;

    private String body;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
