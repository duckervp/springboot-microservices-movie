package com.duckervn.movieservice.controller;

import com.duckervn.movieservice.common.RespMessage;
import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.domain.model.addepisode.EpisodeInput;
import com.duckervn.movieservice.service.IEpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/episodes")
@RequiredArgsConstructor
public class EpisodeController {
    private final IEpisodeService episodeService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody EpisodeInput episodeInput) {
        Response response = Response.builder().code(HttpStatus.CREATED.value())
                .result(episodeService.save(episodeInput))
                .message(RespMessage.CREATED_EPISODE).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PatchMapping("/{episodeId}")
    public ResponseEntity<?> update(@PathVariable Long episodeId, @RequestBody EpisodeInput episodeInput) {
        Response response = Response.builder()
                .code(HttpStatus.OK.value())
                .result(episodeService.update(episodeId, episodeInput))
                .message(RespMessage.UPDATED_EPISODE).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{episodeId}")
    public ResponseEntity<?> delete(@PathVariable Long episodeId) {
        episodeService.delete(episodeId);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_EPISODE).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam List<Long> episodeIds) {
        episodeService.delete(episodeIds);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_EPISODES).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findEpisodeById(@PathVariable Long id) {
        Response response = Response.builder()
                .code(HttpStatus.OK.value())
                .message(RespMessage.FOUND_EPISODE)
                .result(episodeService.findById(id)).build();
        return ResponseEntity.ok(response);
    }
}
