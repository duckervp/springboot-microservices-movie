package com.duckervn.activityservice.service;

import com.duckervn.activityservice.common.Response;
import com.duckervn.activityservice.domain.entity.History;
import com.duckervn.activityservice.domain.model.addhistory.HistoryInput;
import com.duckervn.activityservice.domain.model.addloginactivity.LoginActivityInput;

import java.util.List;

public interface IHistoryService {

    History save(HistoryInput historyInput);

    List<?> findUserHistory(String userId);
}
