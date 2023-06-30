package com.duckervn.cronservice.config;

import com.duckervn.cronservice.service.TaskSchedulingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskInitializer {
    private final TaskSchedulingService taskSchedulingService;

    @PostConstruct
    public void init() {
        int initializedTasks = taskSchedulingService.loadTask();
        log.info("{} tasks initialized!", initializedTasks);
    }
}
