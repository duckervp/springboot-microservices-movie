package com.duckervn.campaignservice.domain.model.addcampaignrecipient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignRecipientInput {
    private Long id;

    @NotNull(message = "campaignId must not be null")
    private Long campaignId;

    @NotNull(message = "recipientId must not be null")
    private String recipientId;

    private LocalDateTime queueDate;

    private String status;

    private LocalDateTime sendDate;

    private Integer retry;

    private String fixedParams;
}
