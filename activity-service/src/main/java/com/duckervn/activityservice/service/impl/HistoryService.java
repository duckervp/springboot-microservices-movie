package com.duckervn.activityservice.service.impl;

import com.duckervn.activityservice.common.Response;
import com.duckervn.activityservice.common.TypeRef;
import com.duckervn.activityservice.config.ServiceConfig;
import com.duckervn.activityservice.domain.entity.History;
import com.duckervn.activityservice.domain.model.addhistory.HistoryInput;
import com.duckervn.activityservice.queue.EventProducer;
import com.duckervn.activityservice.repository.HistoryRepository;
import com.duckervn.activityservice.service.IHistoryService;
import com.duckervn.activityservice.service.client.MovieClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryService implements IHistoryService {

    private final HistoryRepository historyRepository;

    private final ObjectMapper objectMapper;

    private final EventProducer eventProducer;

    private final ServiceConfig serviceConfig;

    private final MovieClient movieClient;

    @Override
    public History save(HistoryInput historyInput) {

        History history = objectMapper.convertValue(historyInput, History.class);

        history.setCreatedAt(LocalDateTime.now());

        historyRepository.save(history);

        updateEpisodeView(history);

        return history;
    }

    @Override
    public List<?> findUserHistory(String userId) {
        List<History> userHistories = historyRepository.findUserWatchedHistory(userId);
        Map<Long, History> movieHistoryMap = new HashMap<>();
        userHistories.forEach(history -> movieHistoryMap.put(history.getMovieId(), history));
        List<Long> movieIds = userHistories.stream().map(History::getMovieId).collect(Collectors.toList());
        Response response = movieClient.findByIds(movieIds);

        List<Map<String, Object>> movies = objectMapper.convertValue(response.getResults(), TypeRef.LIST_MAP_STRING_OBJECT);

        List<Map<String, Object>> results = new ArrayList<>();

        for (Long movieId : movieIds) {
            for (Map<String, Object> movie : movies) {
                Long movieId1 = ((Integer) movie.get("id")).longValue();
                if (movieId.equals(movieId1)) {
                    results.add(movie);
                    break;
                }
            }
        }

        for (Map<String, Object> movie : results) {
            Long movieId = ((Integer) movie.get("id")).longValue();
            if (movieHistoryMap.containsKey(movieId)) {
                List<Map<String, Object>> episodes = objectMapper.convertValue(movie.get("episodes"), TypeRef.LIST_MAP_STRING_OBJECT);
                episodes.sort((ep1, ep2) -> Math.toIntExact(((Integer) ep1.get("id")).longValue() - ((Integer) ep2.get("id")).longValue()));
                boolean ready = false;
                for (Map<String, Object> ep : episodes) {
                    if (ready) {
                        movie.put("nextEpisode", ep);
                        break;
                    }
                    if (((Integer) ep.get("id")).longValue() == movieHistoryMap.get(movieId).getEpisodeId()) {
                        movie.put("lastWatchedEpisode", ep);
                        ready = true;
                    }
                }
                movie.put("lastWatchedTime", movieHistoryMap.get(movieId).getCreatedAt());
            }
            movie.remove("episodes");
            movie.remove("producer");
            movie.remove("characters");
            movie.remove("genres");
            movie.remove("modifiedAt");
            movie.remove("createdAt");
            movie.remove("releaseYear");
            movie.remove("totalEpisode");
            movie.remove("country");
            movie.remove("description");
            movie.remove("rating");
        }

        return results;
    }

    private void updateEpisodeView(History history) {
        Map<String, Object> data = new HashMap<>();
        data.put("movieId", history.getMovieId());
        data.put("episodeId", history.getEpisodeId());
        eventProducer.publish(serviceConfig.getMovieTopic(), serviceConfig.getUpdateEpisodeViewEvent(), data);
    }

}
