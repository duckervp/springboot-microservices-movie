package com.duckervn.campaignservice.domain.model.getcampaign;

import lombok.Data;

@Data
public class ProviderOutput {
    private Long id;

    private String name;

    private String status;

    private String type;

    private String sendMethod;
}
