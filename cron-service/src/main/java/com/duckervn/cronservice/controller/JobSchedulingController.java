package com.duckervn.cronservice.controller;

import com.duckervn.cronservice.common.RespMessage;
import com.duckervn.cronservice.common.Response;
import com.duckervn.cronservice.domain.entity.Task;
import com.duckervn.cronservice.service.TaskSchedulingService;
import com.duckervn.cronservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/schedules")
@RequiredArgsConstructor
public class JobSchedulingController {

    private final TaskSchedulingService taskSchedulingService;

    private final TaskService taskService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping
    public Mono<ResponseEntity<Response>> getScheduledTasks() {
        return Mono.just(ResponseEntity.ok(Response.builder()
                .code(HttpStatus.OK.value())
                .message(RespMessage.FOUND_CRON)
                .results(taskService.findAll())
                .build()));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public Mono<ResponseEntity<Response>> scheduleTask(@RequestBody Task task) {
        return Mono.just(ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.builder()
                        .code(HttpStatus.CREATED.value())
                        .message(RespMessage.ADDED_CRON)
                        .result(taskSchedulingService.scheduleTask(task))
                        .build()));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PatchMapping("/{taskId}")
    public Mono<ResponseEntity<Response>> updateTask(@PathVariable String taskId, @RequestBody Task task) {
        return Mono.just(ResponseEntity.ok(Response.builder()
                .code(HttpStatus.OK.value())
                .message(RespMessage.UPDATED_CRON)
                .result(taskSchedulingService.updateTask(taskId, task))
                .build()));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{taskId}")
    public Mono<ResponseEntity<Response>> removeTask(@PathVariable String taskId) {
        taskSchedulingService.removeScheduledTask(taskId);
        return Mono.just(ResponseEntity.ok(Response.builder()
                .code(HttpStatus.OK.value())
                .message(RespMessage.DELETED_CRON)
                .build()));
    }
}
