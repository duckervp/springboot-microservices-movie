package com.duckervn.movieservice.controller;

import com.duckervn.movieservice.domain.model.addcharacter.CharacterInput;
import com.duckervn.movieservice.service.ICharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/characters")
@RequiredArgsConstructor
public class CharacterController {
    private final ICharacterService characterService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid CharacterInput characterInput) {
        return new ResponseEntity<>(characterService.save(characterInput), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PatchMapping("/{characterId}")
    public ResponseEntity<?> update(@PathVariable Long characterId, @RequestBody @Valid CharacterInput characterInput) {
        return ResponseEntity.ok(characterService.update(characterId, characterInput));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{characterId}")
    public ResponseEntity<?> delete(@PathVariable Long characterId) {
        return ResponseEntity.ok(characterService.delete(characterId));
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(characterService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(characterService.findCharacter(id));
    }
}
