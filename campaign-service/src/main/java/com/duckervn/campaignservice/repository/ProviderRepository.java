package com.duckervn.campaignservice.repository;

import com.duckervn.campaignservice.domain.entity.Campaign;
import com.duckervn.campaignservice.domain.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

}
