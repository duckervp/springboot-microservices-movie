package com.duckervn.campaignservice.service.impl;

import com.duckervn.campaignservice.common.Constants;
import com.duckervn.campaignservice.common.Response;
import com.duckervn.campaignservice.domain.entity.Campaign;
import com.duckervn.campaignservice.domain.entity.CampaignRecipient;
import com.duckervn.campaignservice.domain.entity.Provider;
import com.duckervn.campaignservice.domain.exception.ResourceNotFoundException;
import com.duckervn.campaignservice.domain.model.startcampaign.SendCampaignOutput;
import com.duckervn.campaignservice.repository.CampaignRecipientRepository;
import com.duckervn.campaignservice.repository.CampaignRepository;
import com.duckervn.campaignservice.repository.ProviderRepository;
import com.duckervn.campaignservice.service.IMessageService;
import com.duckervn.campaignservice.service.VendorSelectorService;
import com.duckervn.campaignservice.service.client.UserClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailgun.model.message.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {

    private final CampaignRepository campaignRepository;

    private final CampaignRecipientRepository campaignRecipientRepository;

    private final UserClient userClient;

    private final ProviderRepository providerRepository;

    private final VendorSelectorService vendorSelectorService;

    private final ObjectMapper objectMapper;

    @Override
    public Campaign getCampaignDetail(Long campaignId) {
        return campaignRepository.findById(campaignId).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public List<CampaignRecipient> getCampaignRecipients(Long campaignId) {
        List<String> allowedStatuses = List.of(Constants.WAITING, Constants.FAILED);
        return campaignRecipientRepository.findByCampaignIdAndStatusInAndRetryLessThan(campaignId, allowedStatuses, Constants.MAX_RETRY);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getDataMapping(CampaignRecipient campaignRecipient) {
        Response response = userClient.findUserById(campaignRecipient.getRecipientId());
        Map<String, Object> user = (Map<String, Object>) response.getResult();
        Map<String, Object> mapping = new HashMap<>(user);
        if (Objects.nonNull(campaignRecipient.getFixedParams())) {
            Map<String, Object> fixedParams = objectMapper.readValue(campaignRecipient.getFixedParams(), Map.class);
            mapping.putAll(fixedParams);
        }
        return mapping;
    }

    @Override
    public Provider getProvider(Long providerId) {
        return providerRepository.findById(providerId).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public Object send(Provider provider, Map<String, Object> recipient, String subject, String body) {
        return vendorSelectorService.getVendor(provider).send(provider, recipient,subject, body);
    }

    @Override
    public SendCampaignOutput finishCampaign(Campaign campaign, CampaignRecipient recipient, Provider provider, Object sendResult) {
        boolean success = false;

        if (provider.getType().equals(Constants.EMAIL)) {
            if (provider.getSendMethod().equals(Constants.API)) {
                MessageResponse result = (MessageResponse) sendResult;
                if (Objects.nonNull(result)) {
                    success = true;
                }
            } else {
                success = (Boolean) sendResult;
            }
        }

        recipient.setSendDate(LocalDateTime.now());
        if (success) {
            recipient.setStatus(Constants.SUCCEEDED);
        } else {
            recipient.setStatus(Constants.FAILED);
            recipient.setRetry(recipient.getRetry() + 1);
        }

        recipient.setModifiedAt(LocalDateTime.now());

        campaignRecipientRepository.save(recipient);

        return objectMapper.convertValue(recipient, SendCampaignOutput.class);
    }
}
