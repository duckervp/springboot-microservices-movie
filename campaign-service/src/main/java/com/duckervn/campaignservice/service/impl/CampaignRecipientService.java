package com.duckervn.campaignservice.service.impl;

import com.duckervn.campaignservice.common.Constants;
import com.duckervn.campaignservice.common.RespMessage;
import com.duckervn.campaignservice.common.Response;
import com.duckervn.campaignservice.common.TypeRef;
import com.duckervn.campaignservice.config.ServiceConfig;
import com.duckervn.campaignservice.domain.entity.CampaignRecipient;
import com.duckervn.campaignservice.domain.exception.ResourceNotFoundException;
import com.duckervn.campaignservice.domain.model.addcampaignrecipient.CampaignRecipientInput;
import com.duckervn.campaignservice.queue.EventProducer;
import com.duckervn.campaignservice.repository.CampaignRecipientRepository;
import com.duckervn.campaignservice.repository.CampaignRepository;
import com.duckervn.campaignservice.service.ICampaignRecipientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignRecipientService implements ICampaignRecipientService {

    private final CampaignRecipientRepository campaignRecipientRepository;

    private final CampaignRepository campaignRepository;

    private final EventProducer eventProducer;

    private final ObjectMapper objectMapper;

    private final ServiceConfig serviceConfig;

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

    @SneakyThrows
    private void validateRecipientId(String userId, boolean isRequired) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("userId", userId);

        Map<String, Object> resultMap = eventProducer.publishAndWait(
                serviceConfig.getUserTopic(),
                serviceConfig.getUserToCampaignReplyTopic(),
                serviceConfig.getCheckUserExistEvent(),
                requestMap);

        log.info("Result back: {}", resultMap);

        boolean isExist = false;

        if (Objects.nonNull(resultMap) && Objects.nonNull(resultMap.get(Constants.DATA_ATTR))) {
            Map<String, Object> data = objectMapper.readValue((String) resultMap.get(Constants.DATA_ATTR), TypeRef.MAP_STRING_OBJECT);
            String userId1 = (String) data.get("userId");
            isExist = (Boolean) data.get("exist");
            if (Objects.isNull(userId1) || !userId1.equals(userId) || !isExist) {
                isExist = false;
            }
        }

        if ((Objects.nonNull(userId) && !isExist || (Objects.isNull(userId) && isRequired))) {
            throw new ResourceNotFoundException();
        }
    }
}
