package com.duckervn.activityservice.controller;

import com.duckervn.activityservice.common.RespMessage;
import com.duckervn.activityservice.common.Response;
import com.duckervn.activityservice.domain.model.addhistory.HistoryInput;
import com.duckervn.activityservice.service.IHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activities/history")
public class HistoryController {
    private final IHistoryService historyService;

    @PostMapping
    public ResponseEntity<?> addHistoryActivity(@RequestBody HistoryInput input) {
        Response response = Response.builder()
                .code(HttpStatus.CREATED.value())
                .message(RespMessage.ADDED_HISTORY)
                .result(historyService.save(input)).build();
        return ResponseEntity.ok(response);
    }

}
