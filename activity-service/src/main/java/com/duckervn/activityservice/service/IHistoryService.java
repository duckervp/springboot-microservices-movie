package com.duckervn.activityservice.service;

import com.duckervn.activityservice.common.Response;
import com.duckervn.activityservice.domain.model.addhistory.HistoryInput;
import com.duckervn.activityservice.domain.model.addloginactivity.LoginActivityInput;

public interface IHistoryService {

    Response save(HistoryInput historyInput);
}
