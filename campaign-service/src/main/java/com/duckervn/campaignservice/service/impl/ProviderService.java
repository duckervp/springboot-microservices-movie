package com.duckervn.campaignservice.service.impl;

import com.duckervn.campaignservice.common.Constants;
import com.duckervn.campaignservice.domain.entity.Provider;
import com.duckervn.campaignservice.domain.exception.ResourceNotFoundException;
import com.duckervn.campaignservice.domain.model.addprovider.ProviderInput;
import com.duckervn.campaignservice.repository.ProviderRepository;
import com.duckervn.campaignservice.service.IProviderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProviderService implements IProviderService {

    private final ProviderRepository providerRepository;

    private final ObjectMapper objectMapper;

    @Override
    public List<Provider> findAll() {
        return providerRepository.findAll();
    }

    @Override
    public Provider findById(Long providerId) {
        return providerRepository.findById(providerId).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public Provider save(ProviderInput providerInput) {
        validateType(providerInput.getType(), true);
        validateStatus(providerInput.getStatus(), true);
        validateSendMethod(providerInput.getSendMethod(), true);

        Provider provider = objectMapper.convertValue(providerInput, Provider.class);

        provider.setCreatedAt(LocalDateTime.now());
        provider.setModifiedAt(LocalDateTime.now());

        providerRepository.save(provider);

        return provider;
    }

    @Override
    public Provider update(Long providerId, ProviderInput providerInput) {
        validateType(providerInput.getType(), false);
        validateStatus(providerInput.getStatus(), false);
        validateSendMethod(providerInput.getSendMethod(), false);

        Provider provider = providerRepository.findById(providerId).orElseThrow(ResourceNotFoundException::new);

        if (StringUtils.isNotBlank(providerInput.getName())) {
            provider.setName(providerInput.getName());
        }

        if (Objects.nonNull(providerInput.getSender())) {
            provider.setSender(providerInput.getSender());
        }

        if (Objects.nonNull(providerInput.getType())) {
            provider.setType(providerInput.getType());
        }

        if (Objects.nonNull(providerInput.getStatus())) {
            provider.setStatus(providerInput.getStatus());
        }

        if (Objects.nonNull(providerInput.getSendMethod())) {
            provider.setSendMethod(providerInput.getSendMethod());
        }

        if (Objects.nonNull(providerInput.getHostname())) {
            provider.setHostname(providerInput.getHostname());
        }

        if (Objects.nonNull(providerInput.getPort())) {
            provider.setPort(providerInput.getPort());
        }

        if (Objects.nonNull(providerInput.getUsername())) {
            provider.setUsername(providerInput.getUsername());
        }

        if (Objects.nonNull(providerInput.getPassword())) {
            provider.setPassword(providerInput.getPassword());
        }

        if (Objects.nonNull(providerInput.getApiDomainName())) {
            provider.setApiDomainName(providerInput.getApiDomainName());
        }

        if (Objects.nonNull(providerInput.getApiKey())) {
            provider.setApiKey(providerInput.getApiKey());
        }

        provider.setModifiedAt(LocalDateTime.now());

        providerRepository.save(provider);

        return provider;
    }

    @Override
    public void delete(Long providerId) {
        Provider provider = providerRepository.findById(providerId).orElseThrow(ResourceNotFoundException::new);

        providerRepository.delete(provider);
    }

    @Override
    public void delete(List<Long> providerIds) {
        providerRepository.deleteAllById(providerIds);
    }

    private void validateStatus(String status, boolean isRequired) {
        if ((Objects.nonNull(status) && !Constants.STATUSES.contains(status)) || (Objects.isNull(status) && isRequired)) {
            // throw err
            throw new IllegalArgumentException("Invalid provider status!");
        }
    }

    private void validateType(String type, boolean isRequired) {
        if ((Objects.nonNull(type) && !Constants.MESSENGER_TYPES.contains(type)) || (Objects.isNull(type) && isRequired)) {
            // throw err
            throw new IllegalArgumentException("Invalid provider type!");
        }
    }

    private void validateSendMethod(String method, boolean isRequired) {
        if ((Objects.nonNull(method) && !Constants.SEND_METHODS.contains(method)) || (Objects.isNull(method) && isRequired)) {
            // throw err
            throw new IllegalArgumentException("Invalid provider send method!");
        }
    }
}
