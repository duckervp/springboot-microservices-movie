package com.duckervn.campaignservice.repository;

import com.duckervn.campaignservice.domain.entity.CampaignRecipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignRecipientRepository extends JpaRepository<CampaignRecipient, Long> {

    List<CampaignRecipient> findByCampaignIdAndStatusInAndRetryLessThan(Long campaignId, List<String> statuses, Integer retry);

    List<CampaignRecipient> findByCampaignId(Long campaignId);

    Optional<CampaignRecipient> findByCampaignIdAndId(Long campaignId, Long campaignRecipientId);

}
