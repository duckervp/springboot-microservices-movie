package com.duckervn.activityservice.service;

import com.duckervn.activityservice.domain.entity.Rating;
import com.duckervn.activityservice.domain.model.addrating.RatingInput;

public interface  IRatingService {

    Rating save(RatingInput ratingInput);

    Rating update(Long ratingId, Integer point);

    void delete(Long ratingId);
}
