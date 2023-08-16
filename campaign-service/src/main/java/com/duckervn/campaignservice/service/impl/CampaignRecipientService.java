package com.duckervn.campaignservice.service.impl;

import com.duckervn.campaignservice.common.Constants;
import com.duckervn.campaignservice.common.RespMessage;
import com.duckervn.campaignservice.domain.entity.CampaignRecipient;
import com.duckervn.campaignservice.domain.exception.ResourceNotFoundException;
import com.duckervn.campaignservice.domain.model.addcampaignrecipient.CampaignRecipientInput;
import com.duckervn.campaignservice.repository.CampaignRecipientRepository;
import com.duckervn.campaignservice.repository.CampaignRepository;
import com.duckervn.campaignservice.service.ICampaignRecipientService;
import com.duckervn.campaignservice.service.client.SpecialUserClient;
import com.duckervn.campaignservice.service.client.UserClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignRecipientService implements ICampaignRecipientService {

    private final CampaignRecipientRepository campaignRecipientRepository;

    private final CampaignRepository campaignRepository;

    private final UserClient userClient;

    private final SpecialUserClient specialUserClient;

    private final ObjectMapper objectMapper;

    @Override
    public List<CampaignRecipient> findAll(Long campaignId) {
        return campaignRecipientRepository.findByCampaignId(campaignId);
    }

    @Override
    public CampaignRecipient findByCampaignIdAndRecipientId(Long campaignId, Long campaignRecipientId) {
        return campaignRecipientRepository.findByCampaignIdAndId(campaignId, campaignRecipientId).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public CampaignRecipient save(Long campaignId, CampaignRecipientInput campaignRecipientInput, boolean fromQueue) {
        validateStatus(campaignRecipientInput.getStatus(), true);
        validateCampaignId(campaignId);
        validateRecipientId(campaignRecipientInput.getRecipientId(), true, fromQueue);

        CampaignRecipient campaignRecipient = objectMapper.convertValue(campaignRecipientInput, CampaignRecipient.class);

        campaignRecipient.setCampaignId(campaignId);

        if (Objects.isNull(campaignRecipient.getQueueDate())) {
            campaignRecipient.setQueueDate(LocalDateTime.now());
        }

        campaignRecipient.setCreatedAt(LocalDateTime.now());
        campaignRecipient.setModifiedAt(LocalDateTime.now());

        campaignRecipientRepository.save(campaignRecipient);

        return campaignRecipient;
    }

    @Override
    public CampaignRecipient update(Long campaignId, Long campaignRecipientId, CampaignRecipientInput campaignRecipientInput) {
        validateCampaignId(campaignId);
        CampaignRecipient campaignRecipient = campaignRecipientRepository.findById(campaignRecipientId)
                .orElseThrow(ResourceNotFoundException::new);

        if (!campaignRecipient.getStatus().equals(Constants.WAITING)) {
            throw new IllegalArgumentException(RespMessage.CANNOT_UPDATE_CAMPAIGN_RECIPIENT);
        }

        validateStatus(campaignRecipientInput.getStatus(), false);
        validateRecipientId(campaignRecipientInput.getRecipientId(), false, false);

        if (Objects.nonNull(campaignRecipientInput.getRecipientId())) {
            campaignRecipient.setRecipientId(campaignRecipientInput.getRecipientId());
        }

        if (Objects.nonNull(campaignRecipientInput.getStatus())) {
            campaignRecipient.setStatus(campaignRecipientInput.getStatus());
        }

        if (Objects.nonNull(campaignRecipientInput.getQueueDate())) {
            campaignRecipient.setQueueDate(campaignRecipientInput.getQueueDate());
        }

        if (Objects.nonNull(campaignRecipientInput.getSendDate())) {
            campaignRecipient.setSendDate(campaignRecipientInput.getSendDate());
        }

        if (Objects.nonNull(campaignRecipientInput.getRetry())) {
            campaignRecipient.setRetry(campaignRecipientInput.getRetry());
        }

        if (Objects.nonNull(campaignRecipientInput.getFixedParams())) {
            campaignRecipient.setFixedParams(campaignRecipientInput.getFixedParams());
        }

        campaignRecipient.setModifiedAt(LocalDateTime.now());

        campaignRecipientRepository.save(campaignRecipient);

        return campaignRecipient;
    }

    @Override
    public void delete(Long campaignId, Long campaignRecipientId) {
        CampaignRecipient campaignRecipient = findByCampaignIdAndRecipientId(campaignId, campaignRecipientId);

        campaignRecipientRepository.delete(campaignRecipient);
    }

    private void validateStatus(String status, boolean isRequired) {
        if ((Objects.nonNull(status) && !Constants.CAMPAIGN_RECIPIENT_STATUSES.contains(status))
                || (Objects.isNull(status) && isRequired)) {
            // throw err
            throw new IllegalArgumentException(RespMessage.INVALID_CAMPAIGN_RECIPIENT_STATUS);
        }
    }

    private void validateCampaignId(Long campaignId) {
        if ((Objects.nonNull(campaignId) && !campaignRepository.existsById(campaignId)) || (Objects.isNull(campaignId))) {
            throw new ResourceNotFoundException();
        }
    }

    private void validateRecipientId(String userId, boolean isRequired, boolean fromQueue) {
        if ((Objects.nonNull(userId) && !fromQueue && Objects.isNull(userClient.findUserById(userId).getResult()))
                || (Objects.nonNull(userId) && fromQueue && Objects.isNull(specialUserClient.findUserById(userId).getResult()))
                || (Objects.isNull(userId) && isRequired)) {
            throw new ResourceNotFoundException();
        }
    }
}
