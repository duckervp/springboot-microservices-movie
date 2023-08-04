package com.duckervn.activityservice.service;

import com.duckervn.activityservice.common.Response;
import com.duckervn.activityservice.domain.model.addrating.RatingInput;

public interface  IRatingService {

    Response save(RatingInput ratingInput);

    Response update(Long ratingId, Integer point);

    Response delete(Long ratingId);
}
