package com.duckervn.campaignservice.service;

import com.duckervn.campaignservice.common.Response;
import com.duckervn.campaignservice.domain.model.addprovider.ProviderInput;

public interface IProviderService {
    Response findAll();

    Response findById(Long providerId);

    Response save(ProviderInput providerInput);

    Response update(Long providerId, ProviderInput providerInput);

    Response delete(Long providerId);
}
