package com.duckervn.campaignservice.service.impl;

import com.duckervn.campaignservice.common.Constants;
import com.duckervn.campaignservice.common.RespMessage;
import com.duckervn.campaignservice.common.Response;
import com.duckervn.campaignservice.domain.entity.Campaign;
import com.duckervn.campaignservice.domain.exception.ResourceNotFoundException;
import com.duckervn.campaignservice.domain.model.addcampaign.CampaignInput;
import com.duckervn.campaignservice.repository.CampaignRepository;
import com.duckervn.campaignservice.repository.ProviderRepository;
import com.duckervn.campaignservice.service.ICampaignService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CampaignService implements ICampaignService {

    private final CampaignRepository campaignRepository;

    private final ProviderRepository providerRepository;

    private final ObjectMapper objectMapper;

    @Override
    public Response findAll() {
        return Response.builder().code(HttpStatus.OK.value())
                .message(RespMessage.FOUND_CAMPAIGNS)
                .results(campaignRepository.findAll()).build();
    }

    @Override
    public Response findById(Long campaignId) {
        return Response.builder().code(HttpStatus.OK.value())
                .message(RespMessage.FOUND_CAMPAIGN)
                .result(campaignRepository.findById(campaignId).orElseThrow(ResourceNotFoundException::new))
                .build();
    }

    @Override
    public Response save(CampaignInput campaignInput) {
        validateStatus(campaignInput.getStatus(), true);
        validateType(campaignInput.getType(), true);

        if (Objects.nonNull(campaignInput.getProviderId()) && !providerRepository.existsById(campaignInput.getProviderId())) {
            throw new ResourceNotFoundException();
        }

        Campaign campaign = objectMapper.convertValue(campaignInput, Campaign.class);

        campaign.setCreatedAt(LocalDateTime.now());
        campaign.setModifiedAt(LocalDateTime.now());

        campaignRepository.save(campaign);

        return Response.builder().code(HttpStatus.CREATED.value())
                .message(RespMessage.CREATED_CAMPAIGN)
                .result(campaign).build();
    }

    @Override
    public Response update(Long campaignId, CampaignInput campaignInput) {
        validateStatus(campaignInput.getStatus(), false);
        validateType(campaignInput.getType(), false);

        if (Objects.nonNull(campaignInput.getProviderId()) && !providerRepository.existsById(campaignInput.getProviderId())) {
            throw new ResourceNotFoundException();
        }

        Campaign campaign = campaignRepository.findById(campaignId).orElseThrow(ResourceNotFoundException::new);

        campaign.setProviderId(campaignInput.getProviderId());

        if (StringUtils.isNotBlank(campaignInput.getName())) {
            campaign.setName(campaignInput.getName());
        }

        if (Objects.nonNull(campaignInput.getStatus())) {
            campaign.setStatus(campaignInput.getStatus());
        }

        if (Objects.nonNull(campaignInput.getType())) {
            campaign.setType(campaignInput.getType());
        }

        if (Objects.nonNull(campaignInput.getSubject())) {
            campaign.setSubject(campaignInput.getSubject());
        }

        if (Objects.nonNull(campaignInput.getBody())) {
            campaign.setBody(campaignInput.getBody());
        }

        campaign.setModifiedAt(LocalDateTime.now());

        campaignRepository.save(campaign);

        return Response.builder().code(HttpStatus.OK.value())
                .message(RespMessage.UPDATED_CAMPAIGN)
                .result(campaign).build();
    }

    @Override
    public Response delete(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId).orElseThrow(ResourceNotFoundException::new);

        campaignRepository.delete(campaign);

        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_CAMPAIGN).build();
    }

    private void validateStatus(String status, boolean isRequired) {
        if ((Objects.nonNull(status) && !Constants.STATUSES.contains(status)) || (Objects.isNull(status) && isRequired)) {
            // throw err
            throw new IllegalArgumentException(RespMessage.INVALID_CAMPAIGN_STATUS);
        }
    }

    private void validateType(String type, boolean isRequired) {
        if ((Objects.nonNull(type) && !Constants.MESSENGER_TYPES.contains(type)) || (Objects.isNull(type) && isRequired)) {
            // throw err
            throw new IllegalArgumentException(RespMessage.INVALID_CAMPAIGN_TYPE);
        }
    }
}
