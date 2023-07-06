package com.duckervn.campaignservice.service;

import com.duckervn.campaignservice.common.Constants;
import com.duckervn.campaignservice.domain.entity.Provider;
import com.duckervn.campaignservice.service.impl.MailgunService;
import org.springframework.stereotype.Service;

@Service
public class VendorSelectorService {
    public IVendorService getVendor(Provider provider) {
        if (provider.getType().equals(Constants.EMAIL)) {
            return new MailgunService();
        }
        else return null;
    }
}
