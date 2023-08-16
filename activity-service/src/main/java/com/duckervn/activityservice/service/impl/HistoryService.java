package com.duckervn.activityservice.service.impl;

import com.duckervn.activityservice.config.ServiceConfig;
import com.duckervn.activityservice.domain.entity.History;
import com.duckervn.activityservice.domain.model.addhistory.HistoryInput;
import com.duckervn.activityservice.queue.EventProducer;
import com.duckervn.activityservice.repository.HistoryRepository;
import com.duckervn.activityservice.service.IHistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HistoryService implements IHistoryService {

    private final HistoryRepository historyRepository;

    private final ObjectMapper objectMapper;

    private final EventProducer eventProducer;

    private final ServiceConfig serviceConfig;

    @Override
    public History save(HistoryInput historyInput) {

        History history = objectMapper.convertValue(historyInput, History.class);

        history.setCreatedAt(LocalDateTime.now());

        historyRepository.save(history);

        updateEpisodeView(history);

        return history;
    }

    private void updateEpisodeView(History history) {
        Map<String, Object> data = new HashMap<>();
        data.put("movieId", history.getMovieId());
        data.put("episodeId", history.getEpisodeId());
        eventProducer.publish(serviceConfig.getMovieTopic(), serviceConfig.getUpdateEpisodeViewEvent(), data);
    }

}
