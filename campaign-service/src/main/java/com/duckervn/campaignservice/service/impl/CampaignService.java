package com.duckervn.campaignservice.service.impl;

import com.duckervn.campaignservice.common.Constants;
import com.duckervn.campaignservice.common.RespMessage;
import com.duckervn.campaignservice.domain.entity.Campaign;
import com.duckervn.campaignservice.domain.entity.Provider;
import com.duckervn.campaignservice.domain.exception.ResourceNotFoundException;
import com.duckervn.campaignservice.domain.model.addcampaign.CampaignInput;
import com.duckervn.campaignservice.domain.model.getcampaign.CampaignOutput;
import com.duckervn.campaignservice.domain.model.getcampaign.ProviderOutput;
import com.duckervn.campaignservice.domain.model.page.PageOutput;
import com.duckervn.campaignservice.repository.CampaignRepository;
import com.duckervn.campaignservice.repository.ProviderRepository;
import com.duckervn.campaignservice.service.ICampaignService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignService implements ICampaignService {

    private final CampaignRepository campaignRepository;

    private final ProviderRepository providerRepository;

    private final ObjectMapper objectMapper;

    @Override
    public PageOutput<CampaignOutput> findAllCampaignOutput(Integer pageNo, Integer pageSize) {
        if (pageNo <= 0) {
            throw new IllegalArgumentException("pageNo start from one.");
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Campaign> campaigns = campaignRepository.findAll(pageable);
        Set<Long> providerIds = campaigns.getContent()
                .stream().map(Campaign::getProviderId).collect(Collectors.toSet());
        List<ProviderOutput> providerOutputs = providerRepository.findAllById(providerIds)
                .stream().map(provider -> objectMapper.convertValue(provider, ProviderOutput.class))
                .collect(Collectors.toList());
        List<CampaignOutput> campaignOutputs = new ArrayList<>();
        for (Campaign campaign : campaigns.getContent()) {
            CampaignOutput campaignOutput = objectMapper.convertValue(campaign, CampaignOutput.class);
            for (ProviderOutput providerOutput : providerOutputs) {
                if (Objects.nonNull(campaign.getProviderId()) && campaign.getProviderId().equals(providerOutput.getId())) {
                    campaignOutput.setProvider(providerOutput);
                    break;
                }
            }
            campaignOutputs.add(campaignOutput);
        }
        return new PageOutput<>(campaignOutputs, pageNo, pageSize, campaigns.getTotalElements());
    }

    @Override
    public CampaignOutput findById(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId).orElseThrow(ResourceNotFoundException::new);
        return convert(campaign, null);
    }

    @Override
    public CampaignOutput save(CampaignInput campaignInput) {
        validateStatus(campaignInput.getStatus(), true);
        validateType(campaignInput.getType(), true);

        Provider provider = providerRepository.findById(campaignInput.getProviderId())
                .orElseThrow(ResourceNotFoundException::new);

        Campaign campaign = objectMapper.convertValue(campaignInput, Campaign.class);

        campaign.setCreatedAt(LocalDateTime.now());
        campaign.setModifiedAt(LocalDateTime.now());

        campaignRepository.save(campaign);

        return convert(campaign, provider);
    }

    @Override
    public CampaignOutput update(Long campaignId, CampaignInput campaignInput) {
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

        return convert(campaign, null);
    }

    @Override
    public void delete(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId).orElseThrow(ResourceNotFoundException::new);

        campaignRepository.delete(campaign);
    }

    @Override
    public void delete(List<Long> campaignIds) {
        campaignRepository.deleteAllById(campaignIds);
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

    private CampaignOutput convert(Campaign campaign, Provider provider) {
        CampaignOutput campaignOutput = objectMapper.convertValue(campaign, CampaignOutput.class);
        if (Objects.isNull(provider)) {
            provider = providerRepository.findById(campaign.getProviderId())
                    .orElseThrow(ResourceNotFoundException::new);
        }
        ProviderOutput providerOutput = objectMapper.convertValue(provider, ProviderOutput.class);
        campaignOutput.setProvider(providerOutput);
        return campaignOutput;
    }
}
