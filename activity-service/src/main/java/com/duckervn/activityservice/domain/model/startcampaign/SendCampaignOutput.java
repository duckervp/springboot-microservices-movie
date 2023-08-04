package com.duckervn.activityservice.domain.model.startcampaign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SendCampaignOutput {
    private Long id;

    private Long campaignId;

    private String recipientId;

    private String status;

    private LocalDateTime sendDate;
}
