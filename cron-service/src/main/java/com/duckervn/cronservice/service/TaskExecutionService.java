package com.duckervn.cronservice.service;

import com.duckervn.cronservice.domain.entity.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationManager;

@Slf4j
public class TaskExecutionService implements Runnable {
    private final Task task;

    private final ClientService clientService;

    public TaskExecutionService(Task task, ClientService clientService) {
        this.task = task;
        this.clientService = clientService;
    }

    @Override
    public void run() {
        log.info("Running Task[id={}, method={}, url={}]", task.getId(), task.getMethod(), task.getUrl());
        clientService.makeRequest(HttpMethod.resolve(task.getMethod()), task.getUrl());
    }

}
