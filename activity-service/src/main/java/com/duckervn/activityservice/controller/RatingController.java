package com.duckervn.activityservice.controller;

import com.duckervn.activityservice.domain.model.addrating.RatingInput;
import com.duckervn.activityservice.service.IRatingService;
import lombok.RequiredArgsConstructor;
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
        return ResponseEntity.ok(ratingService.save(input));
    }

    @PatchMapping("/{ratingId}")
    public ResponseEntity<?> editRating(@PathVariable Long ratingId, @RequestParam Integer point) {
        return ResponseEntity.ok(ratingService.update(ratingId, point));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{ratingId}")
    public ResponseEntity<?> removeRating(@PathVariable Long ratingId) {
        return ResponseEntity.ok(ratingService.delete(ratingId));
    }

}
