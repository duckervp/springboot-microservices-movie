package com.duckervn.activityservice.controller;

import com.duckervn.activityservice.common.RespMessage;
import com.duckervn.activityservice.common.Response;
import com.duckervn.activityservice.domain.model.addrating.RatingInput;
import com.duckervn.activityservice.service.IRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activities/rating")
public class RatingController {
    private final IRatingService ratingService;

    @PostMapping
    public ResponseEntity<?> addRating(@RequestBody @Valid RatingInput input) {
        Response response = Response.builder().code(HttpStatus.CREATED.value())
                .message(RespMessage.CREATED_RATING)
                .result(ratingService.save(input)).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{ratingId}")
    public ResponseEntity<?> editRating(@PathVariable Long ratingId, @RequestParam Integer point) {
        Response response = Response.builder()
                .code(HttpStatus.OK.value())
                .message(RespMessage.UPDATED_RATING)
                .result(ratingService.update(ratingId, point))
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{ratingId}")
    public ResponseEntity<?> removeRating(@PathVariable Long ratingId) {
        ratingService.delete(ratingId);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_RATING).build();
        return ResponseEntity.ok(response);
    }

}
