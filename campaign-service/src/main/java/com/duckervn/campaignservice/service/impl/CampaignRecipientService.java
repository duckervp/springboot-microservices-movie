package com.duckervn.campaignservice.service.impl;

import com.duckervn.campaignservice.common.Constants;
import com.duckervn.campaignservice.common.RespMessage;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
    public List<CampaignRecipient> findAll(Long campaignId) {
        return campaignRecipientRepository.findByCampaignId(campaignId);
    }

    @Override
    public CampaignRecipient findByCampaignIdAndRecipientId(Long campaignId, Long campaignRecipientId) {
        return campaignRecipientRepository.findByCampaignIdAndId(campaignId, campaignRecipientId).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public CampaignRecipient save(Long campaignId, CampaignRecipientInput campaignRecipientInput) {
        validateStatus(campaignRecipientInput.getStatus(), true);
        validateCampaignId(campaignId);
        validateRecipientId(campaignRecipientInput.getRecipientId(), true);

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
        validateRecipientId(campaignRecipientInput.getRecipientId(), false);

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
