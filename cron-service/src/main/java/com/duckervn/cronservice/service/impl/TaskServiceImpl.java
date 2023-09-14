package com.duckervn.cronservice.service.impl;

import com.duckervn.cronservice.common.Constants;
import com.duckervn.cronservice.domain.entity.Task;
import com.duckervn.cronservice.domain.exception.InvalidCronExpression;
import com.duckervn.cronservice.repository.TaskRepository;
import com.duckervn.cronservice.service.TaskService;
import com.duckervn.cronservice.domain.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task findById(String taskId) {
        return taskRepository.findById(taskId).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public Task add(Task task) {
        String newTaskId = UUID.randomUUID().toString();

        log.info("Adding Task[id={}, name={}, cron expression={}, url={}, method={}]", newTaskId, task.getName(), task.getCronExpression(), task.getUrl(), task.getMethod());

        if (!CronExpression.isValidExpression(task.getCronExpression())) {
            throw new InvalidCronExpression("Invalid cron expression!");
        }

        if (Objects.isNull(task.getMethod()) ||
                !Arrays.asList(HttpMethod.values()).contains(HttpMethod.resolve(task.getMethod().toUpperCase()))) {
            throw new IllegalArgumentException("Invalid http method!");
        } else {
            task.setMethod(task.getMethod().toUpperCase());
        }

        if (Objects.isNull(task.getStatus()) || !Constants.STATUSES.contains(task.getStatus())) {
            throw new IllegalArgumentException("Invalid task status!");
        } else {
            task.setStatus(task.getStatus().toUpperCase());
        }

        task.setId(newTaskId);

        task.setCreatedAt(LocalDateTime.now());
        task.setModifiedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    @Override
    public void delete(String taskId) {
        log.info("Deleting Task[id={}]", taskId);
        Task task = taskRepository.findById(taskId).orElseThrow(ResourceNotFoundException::new);
        taskRepository.delete(task);
    }

    @Override
    public void delete(List<String> taskIds) {
        log.info("Deleting Tasks[ids={}]", String.join(", ", taskIds));
        taskRepository.deleteAllById(taskIds);
    }

    @Override
    public Task edit(String taskId, Task task) {
        log.info("Updating Task[id={}]", taskId);
        Task task1 = taskRepository.findById(taskId).orElseThrow(ResourceNotFoundException::new);

        if (StringUtils.isNotBlank(task.getName())) {
            task1.setName(task.getName());
        }

        if (Objects.nonNull(task.getCronExpression())) {
            if (!CronExpression.isValidExpression(task.getCronExpression())) {
                throw new InvalidCronExpression("Invalid cron expression!");
            }
            task1.setCronExpression(task.getCronExpression());
        }

        if (Objects.nonNull(task.getUrl())) {
            task1.setUrl(task.getUrl());
        }

        if (StringUtils.isNotBlank(task.getMethod())) {
            String method = task.getMethod().toUpperCase();
            if (!Arrays.asList(HttpMethod.values()).contains(HttpMethod.resolve(method))) {
                throw new IllegalArgumentException("Invalid Http Method!");
            }
            task1.setMethod(method);
        }

        if (StringUtils.isNotBlank(task.getStatus())) {
            String status = task.getStatus().toUpperCase();
            if (!Constants.STATUSES.contains(task.getStatus())) {
                throw new IllegalArgumentException("Invalid task status!");
            }
            task1.setStatus(status);
        }

        task1.setModifiedAt(LocalDateTime.now());

        return taskRepository.save(task1);
    }
}
