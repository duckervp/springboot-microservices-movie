package com.duckervn.activityservice.service.impl;

import com.duckervn.activityservice.domain.entity.History;
import com.duckervn.activityservice.domain.model.addhistory.HistoryInput;
import com.duckervn.activityservice.repository.HistoryRepository;
import com.duckervn.activityservice.service.IHistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HistoryService implements IHistoryService {

    private final HistoryRepository historyRepository;

    private final ObjectMapper objectMapper;

    @Override
    public History save(HistoryInput historyInput) {

        History history = objectMapper.convertValue(historyInput, History.class);

        history.setCreatedAt(LocalDateTime.now());

        historyRepository.save(history);

        // TODO: public update movie view ?

        return history;
    }

}
