package com.duckervn.campaignservice.domain.model.addcampaign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignInput {
    private Long id;

    private String name;

    private String description;

    private String status;

    private String type;

    private Long providerId;

    private String subject;

    private String body;
}
