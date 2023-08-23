package com.duckervn.activityservice.service.impl;

import com.duckervn.activityservice.config.ServiceConfig;
import com.duckervn.activityservice.domain.entity.Rating;
import com.duckervn.activityservice.domain.exception.ResourceNotFoundException;
import com.duckervn.activityservice.domain.model.addrating.RatingInput;
import com.duckervn.activityservice.queue.EventProducer;
import com.duckervn.activityservice.repository.RatingRepository;
import com.duckervn.activityservice.service.IRatingService;
import com.duckervn.activityservice.service.client.MovieClient;
import com.duckervn.activityservice.service.client.UserClient;
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

    private final MovieClient movieClient;

    private final UserClient userClient;

    @Override
    public Rating save(RatingInput ratingInput) {
        validateUserId(ratingInput.getUserId());
        validateMovieId(ratingInput.getMovieId());

        Rating rating = objectMapper.convertValue(ratingInput, Rating.class);

        rating.setCreatedAt(LocalDateTime.now());
        rating.setModifiedAt(LocalDateTime.now());

        ratingRepository.save(rating);

        // update movie rating point
        updateMovieRating(rating.getMovieId());

        return rating;
    }

    @Override
    public Rating update(Long ratingId, RatingInput ratingInput) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(ResourceNotFoundException::new);

        Integer point = ratingInput.getPoint();

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
    private void validateUserId(String userId) {
        if (Objects.isNull(userId) || userClient.findUserById(userId).getCode() == 404) {
            throw new ResourceNotFoundException();
        }
    }

    @SneakyThrows
    private void validateMovieId(Long movieId) {
        if (Objects.isNull(movieId) || movieClient.findMovieById(movieId).getCode() == 404) {
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

    @Override
    public Rating findByUserIdAndMovieId(String userId, Long movieId) {
        validateUserId(userId);
        validateMovieId(movieId);
        return ratingRepository.findByUserIdAndMovieId(userId, movieId).orElse(new Rating());
    }
}

