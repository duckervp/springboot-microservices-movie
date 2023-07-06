package com.duckervn.campaignservice.service.impl;

import com.duckervn.campaignservice.common.Constants;
import com.duckervn.campaignservice.common.RespMessage;
import com.duckervn.campaignservice.common.Response;
import com.duckervn.campaignservice.domain.entity.CampaignRecipient;
import com.duckervn.campaignservice.domain.exception.ResourceNotFoundException;
import com.duckervn.campaignservice.domain.model.addcampaignrecipient.CampaignRecipientInput;
import com.duckervn.campaignservice.repository.CampaignRecipientRepository;
import com.duckervn.campaignservice.repository.CampaignRepository;
import com.duckervn.campaignservice.service.ICampaignRecipientService;
import com.duckervn.campaignservice.service.client.UserClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CampaignRecipientService implements ICampaignRecipientService {

    private final CampaignRecipientRepository campaignRecipientRepository;

    private final CampaignRepository  campaignRepository;

    private final UserClient userClient;

    private final ObjectMapper objectMapper;

    @Override
    public Response findAll() {
        return Response.builder().code(HttpStatus.OK.value())
                .message(RespMessage.FOUND_CAMPAIGN_RECIPIENTS)
                .results(campaignRecipientRepository.findAll()).build();
    }

    @Override
    public Response findById(Long campaignRecipientId) {
        return Response.builder().code(HttpStatus.OK.value())
                .message(RespMessage.FOUND_CAMPAIGN_RECIPIENT)
                .result(campaignRecipientRepository.findById(campaignRecipientId).orElseThrow(ResourceNotFoundException::new))
                .build();
    }

    @Override
    public Response save(CampaignRecipientInput campaignRecipientInput) {
        validateStatus(campaignRecipientInput.getStatus(), true);
        validateCampaignId(campaignRecipientInput.getCampaignId(), true);
        validateRecipientId(campaignRecipientInput.getRecipientId(), true);

        CampaignRecipient campaignRecipient = objectMapper.convertValue(campaignRecipientInput, CampaignRecipient.class);

        if (Objects.isNull(campaignRecipient.getQueueDate())) {
            campaignRecipient.setQueueDate(LocalDateTime.now());
        }

        campaignRecipient.setCreatedAt(LocalDateTime.now());
        campaignRecipient.setModifiedAt(LocalDateTime.now());

        campaignRecipientRepository.save(campaignRecipient);

        return Response.builder().code(HttpStatus.CREATED.value())
                .message(RespMessage.CREATED_CAMPAIGN_RECIPIENT)
                .result(campaignRecipient).build();
    }

    @Override
    public Response update(Long campaignRecipientId, CampaignRecipientInput campaignRecipientInput) {
        CampaignRecipient campaignRecipient = campaignRecipientRepository.findById(campaignRecipientId)
                .orElseThrow(ResourceNotFoundException::new);

        if (!campaignRecipient.getStatus().equals(Constants.WAITING)) {
            throw new IllegalArgumentException(RespMessage.CANNOT_UPDATE_CAMPAIGN_RECIPIENT);
        }

        validateStatus(campaignRecipientInput.getStatus(), false);
        validateCampaignId(campaignRecipientInput.getCampaignId(), false);
        validateRecipientId(campaignRecipientInput.getRecipientId(), false);

        if (Objects.nonNull(campaignRecipientInput.getCampaignId())) {
            campaignRecipient.setCampaignId(campaignRecipientInput.getCampaignId());
        }

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

        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.UPDATED_CAMPAIGN_RECIPIENT).build();
    }

    @Override
    public Response delete(Long campaignRecipientId) {
        CampaignRecipient campaignRecipient = campaignRecipientRepository.findById(campaignRecipientId)
                .orElseThrow(ResourceNotFoundException::new);

        campaignRecipientRepository.delete(campaignRecipient);

        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_CAMPAIGN_RECIPIENT).build();
    }

    private void validateStatus(String status, boolean isRequired) {
        if ((Objects.nonNull(status) && !Constants.CAMPAIGN_RECIPIENT_STATUSES.contains(status))
                || (Objects.isNull(status) && isRequired)) {
            // throw err
            throw new IllegalArgumentException(RespMessage.INVALID_CAMPAIGN_RECIPIENT_STATUS);
        }
    }

    private void validateCampaignId(Long campaignId, boolean isRequired) {
        if ((Objects.nonNull(campaignId) && !campaignRepository.existsById(campaignId))
                || (Objects.isNull(campaignId) && isRequired)) {
            throw new ResourceNotFoundException();
        }
    }

    private void validateRecipientId(String userId, boolean isRequired) {
        if ((Objects.nonNull(userId) && Objects.isNull(userClient.findUserById(userId).getResult()))
                || (Objects.isNull(userId) && isRequired)) {
            throw new ResourceNotFoundException();
        }
    }
}