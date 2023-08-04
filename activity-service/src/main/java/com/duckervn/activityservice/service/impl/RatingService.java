package com.duckervn.activityservice.service.impl;

import com.duckervn.activityservice.common.RespMessage;
import com.duckervn.activityservice.common.Response;
import com.duckervn.activityservice.config.ServiceConfig;
import com.duckervn.activityservice.domain.entity.Rating;
import com.duckervn.activityservice.domain.exception.ResourceNotFoundException;
import com.duckervn.activityservice.domain.model.addrating.RatingInput;
import com.duckervn.activityservice.queue.EventProducer;
import com.duckervn.activityservice.repository.RatingRepository;
import com.duckervn.activityservice.service.IRatingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingService implements IRatingService {

    private final RatingRepository ratingRepository;

    private final ObjectMapper objectMapper;

    private final ServiceConfig serviceConfig;

    private final EventProducer eventProducer;

    @Override
    public Response save(RatingInput ratingInput) {
        validateMovieId(ratingInput.getMovieId(), true);
        validateRecipientId(ratingInput.getUserId(), true);

        Rating rating = objectMapper.convertValue(ratingInput, Rating.class);

        rating.setCreatedAt(LocalDateTime.now());
        rating.setModifiedAt(LocalDateTime.now());

        ratingRepository.save(rating);

        return Response.builder().code(HttpStatus.CREATED.value())
                .message(RespMessage.CREATED_RATING)
                .result(rating).build();
    }

    @Override
    public Response update(Long ratingId, Integer point) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(ResourceNotFoundException::new);

        if (Objects.nonNull(point) && point >= 0 && point <= 10) {
            rating.setPoint(point);
        }

        rating.setModifiedAt(LocalDateTime.now());

        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.UPDATED_RATING).build();
    }

    @Override
    public Response delete(Long ratingId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(ResourceNotFoundException::new);

        ratingRepository.delete(rating);

        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_RATING).build();
    }

    @SneakyThrows
    private void validateRecipientId(String userId, boolean isRequired) {
        if (Objects.isNull(userId) && isRequired) {
            throw new ResourceNotFoundException();
        } else if (Objects.nonNull(userId)) {
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("userId", userId);

            Map<String, Object> resultMap = eventProducer.publishAndWait(
                    serviceConfig.getUserTopic(),
                    serviceConfig.getUserToActivityReplyTopic(),
                    serviceConfig.getCheckUserExistEvent(),
                    requestMap);

            log.info("Result back: {}", resultMap);

            checkExist(userId, "userId", resultMap);
        }
    }

    @SneakyThrows
    private void validateMovieId(Long movieId, boolean isRequired) {
        if (Objects.isNull(movieId) && isRequired) {
            throw new ResourceNotFoundException();
        } else if (Objects.nonNull(movieId)) {
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("movieId", movieId);

            Map<String, Object> resultMap = eventProducer.publishAndWait(
                    serviceConfig.getMovieTopic(),
                    serviceConfig.getMovieToActivityReplyTopic(),
                    serviceConfig.getCheckMovieExistEvent(),
                    requestMap);

            log.info("Result back: {}", resultMap);

            checkExist(movieId.toString(), "movieId", resultMap);
        }
    }

    private void checkExist(String id, String idFieldName, Map<String, Object> resultMap) {
        boolean isExist = false;

        if (Objects.nonNull(resultMap)) {
            String movieId1 = (String) resultMap.get(idFieldName);
            isExist = (Boolean) resultMap.get("exist");
            if (Objects.isNull(movieId1) || !movieId1.equals(id)) {
                isExist = false;
            }
        }

        if (!isExist) {
            throw new ResourceNotFoundException();
        }
    }

}

