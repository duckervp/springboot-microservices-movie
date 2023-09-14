package com.duckervn.cronservice.service;

import com.duckervn.cronservice.common.Constants;
import com.duckervn.cronservice.domain.entity.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskSchedulingService {

    private final TaskScheduler taskScheduler;

    private final TaskService taskService;

    private final ClientService clientService;

    Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();

    public int loadTask() {
        List<Task> oldTasks = taskService.findAll();

        for (Task task : oldTasks) {
            if (Objects.nonNull(task.getStatus()) && task.getStatus().equalsIgnoreCase(Constants.ACTIVE)) {
                schedule(task);
            }
        }

        return jobsMap.size();
    }

    private void schedule(Task task) {
        if (Objects.nonNull(task.getStatus()) && task.getStatus().equalsIgnoreCase(Constants.ACTIVE)) {
            log.info("Scheduling Task[id={}, cron expression={}, url={}, method={}]", task.getId(), task.getCronExpression(), task.getUrl(), task.getMethod());
            TaskExecutionService taskExecutionService = new TaskExecutionService(task, clientService);
            ScheduledFuture<?> scheduledTask = taskScheduler.schedule(taskExecutionService, new CronTrigger(task.getCronExpression(), TimeZone.getTimeZone(TimeZone.getDefault().getID())));
            jobsMap.put(task.getId(), scheduledTask);
        }
    }

    public Task scheduleTask(Task task) {
        task = taskService.add(task);
        schedule(task);
        return task;
    }


    public Task updateTask(String taskId, Task task) {
        task = taskService.edit(taskId, task);
        stopScheduledTask(taskId);
        schedule(task);
        return task;
    }

    public void removeScheduledTask(String taskId) {
        stopScheduledTask(taskId);
        taskService.delete(taskId);
    }

    public void removeScheduledTasks(List<String> taskIds) {
        for (String taskId : taskIds) {
            stopScheduledTask(taskId);
        }
        taskService.delete(taskIds);
    }

    private void stopScheduledTask(String taskId) {
        log.info("Stopping Task[id={}]", taskId);
        ScheduledFuture<?> scheduledTask = jobsMap.get(taskId);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            jobsMap.put(taskId, null);
        }
    }
}
