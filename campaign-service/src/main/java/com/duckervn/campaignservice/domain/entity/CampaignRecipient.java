package com.duckervn.campaignservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "campaign_recipient")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignRecipient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long campaignId;

    @NotNull
    private String recipientId;

    private LocalDateTime queueDate;

    private String status;

    private LocalDateTime sendDate;

    private Integer retry;

    private String fixedParams;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
