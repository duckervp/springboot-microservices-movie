package com.duckervn.campaignservice.service;

import com.duckervn.campaignservice.common.Response;
import com.duckervn.campaignservice.domain.entity.Provider;
import com.duckervn.campaignservice.domain.model.addprovider.ProviderInput;

import java.util.List;

public interface IProviderService {
    List<Provider> findAll();

    Provider findById(Long providerId);

    Provider save(ProviderInput providerInput);

    Provider update(Long providerId, ProviderInput providerInput);

    void delete(Long providerId);
}
