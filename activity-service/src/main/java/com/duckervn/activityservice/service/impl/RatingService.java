package com.duckervn.activityservice.service.impl;

import com.duckervn.activityservice.common.Constants;
import com.duckervn.activityservice.common.TypeRef;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
    public Rating save(RatingInput ratingInput) {
        validateMovieId(ratingInput.getMovieId());
        validateRecipientId(ratingInput.getUserId());

        Rating rating = objectMapper.convertValue(ratingInput, Rating.class);

        rating.setCreatedAt(LocalDateTime.now());
        rating.setModifiedAt(LocalDateTime.now());

        ratingRepository.save(rating);

        // update movie rating point
        updateMovieRating(rating.getMovieId());

        return rating;
    }

    @Override
    public Rating update(Long ratingId, Integer point) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(ResourceNotFoundException::new);

        if (Objects.nonNull(point) && point >= 0 && point <= 10) {
            rating.setPoint(point);
        }

        rating.setModifiedAt(LocalDateTime.now());

        ratingRepository.save(rating);

        // update movie rating point
        updateMovieRating(rating.getMovieId());

        return rating;
    }

    @Override
    public void delete(Long ratingId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(ResourceNotFoundException::new);

        ratingRepository.delete(rating);
    }

    @SneakyThrows
    private void validateRecipientId(String userId) {
        if (Objects.isNull(userId)) {
            throw new ResourceNotFoundException();
        }
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

    @SneakyThrows
    private void validateMovieId(Long movieId) {
        if (Objects.isNull(movieId)) {
            throw new ResourceNotFoundException();
        }
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

    @SneakyThrows
    private void checkExist(String id, String idFieldName, Map<String, Object> resultMap) {
        boolean isExist = false;

        if (Objects.nonNull(resultMap)) {
            Map<String, Object> data = new HashMap<>();
            if (Objects.nonNull(resultMap.get(Constants.DATA_ATTR))) {
                data = objectMapper.readValue((String) resultMap.get(Constants.DATA_ATTR), TypeRef.MAP_STRING_OBJECT);
            }
            String movieId1 = (String) data.get(idFieldName);
            isExist = (Boolean) data.get("exist");
            if (Objects.isNull(movieId1) || !movieId1.equals(id)) {
                isExist = false;
            }
        }

        if (!isExist) {
            throw new ResourceNotFoundException();
        }
    }

    private void updateMovieRating(Long movieId) {
        Map<String, Object> data = new HashMap<>();
        data.put("movieId", movieId);

        List<Rating> movieRatings = ratingRepository.findByMovieId(movieId);

        double ratingPoint = 0D;

        if (!movieRatings.isEmpty()) {
            int sumOfPoint = movieRatings.stream()
                    .map(Rating::getPoint)
                    .reduce(0, Integer::sum);
            ratingPoint = ((double) sumOfPoint) / movieRatings.size();
        }

        data.put("ratingPoint", ratingPoint);

        eventProducer.publish(serviceConfig.getMovieTopic(), serviceConfig.getUpdateMovieRatingEvent(), data);
    }

}

