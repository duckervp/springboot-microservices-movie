package com.duckervn.campaignservice.service;

import com.duckervn.campaignservice.domain.entity.Provider;

import java.util.Map;

public interface IVendorService {
    Object send(Provider provider, Map<String, Object> recipient, String subject, String body);
}
