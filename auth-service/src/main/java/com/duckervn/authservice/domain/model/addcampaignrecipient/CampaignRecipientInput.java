package com.duckervn.authservice.domain.model.addcampaignrecipient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignRecipientInput {
    private Long id;

    private String recipientId;

    private LocalDateTime queueDate;

    private String status;

    private LocalDateTime sendDate;

    private Integer retry;

    private String fixedParams;
}
