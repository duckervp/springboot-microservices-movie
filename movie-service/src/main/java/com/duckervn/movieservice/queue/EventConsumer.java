package com.duckervn.movieservice.queue;

import com.duckervn.movieservice.common.Constants;
import com.duckervn.movieservice.common.TypeRef;
import com.duckervn.movieservice.config.ServiceConfig;
import com.duckervn.movieservice.repository.MovieRepository;
import com.duckervn.movieservice.service.IFileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventConsumer {

    private final ObjectMapper objectMapper;

    private final IFileService fileService;

    private final ServiceConfig serviceConfig;

    private final MovieRepository movieRepository;

    @SneakyThrows
    @KafkaListener(topics = "${topic.movie}")
    @SendTo
    public String consumeMessageFromQueue(String request) {
        log.info("Consume message: {}", request);

        Map<String, Object> requestMap = objectMapper.readValue(request, TypeRef.MAP_STRING_OBJECT);

        String event = null;

        if (requestMap.containsKey(Constants.EVENT_ATTR)) {
            event = (String) requestMap.get(Constants.EVENT_ATTR);
        }

        Map<String, Object> resultMap = new HashMap<>();

        if (Objects.nonNull(event)) {
            resultMap.put(Constants.EVENT_ATTR, event);
            if (event.equals(serviceConfig.getFindMovieStoredFileEvent())) {
                resultMap.put(Constants.DATA_ATTR, fileService.getAllStoredFiles());
            } else if (event.equals(serviceConfig.getCheckMovieExistEvent())) {
                Long movieId = null;
                boolean exist = false;
                if (requestMap.containsKey("movieId") && Objects.nonNull(requestMap.get("movieId"))) {
                    movieId = Long.valueOf((String) requestMap.get("movieId"));
                }
                if (Objects.nonNull(movieId)) {
                    exist = movieRepository.existsById(movieId);
                }
                Map<String, Object> data = new HashMap<>();
                data.put("movieId", movieId);
                data.put("exist", exist);
                resultMap.put(Constants.DATA_ATTR, data);
            }
        }

        return objectMapper.writeValueAsString(resultMap);
    }
}
