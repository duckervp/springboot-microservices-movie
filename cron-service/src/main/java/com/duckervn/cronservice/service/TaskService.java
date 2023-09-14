package com.duckervn.cronservice.service;

import com.duckervn.cronservice.domain.entity.Task;

import java.util.List;

public interface TaskService {
    List<Task> findAll();

    Task findById(String taskId);

    Task add(Task task);

    void delete(String taskId);

    void delete(List<String> taskIds);

    Task edit(String taskId, Task task);
}
