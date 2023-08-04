package com.duckervn.activityservice.domain.model.addhistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoryInput {

    private String userId;

    private Long movieId;

    private Long episodeId;

}
