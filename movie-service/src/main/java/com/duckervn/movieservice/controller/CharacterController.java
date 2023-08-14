package com.duckervn.movieservice.controller;

import com.duckervn.movieservice.common.RespMessage;
import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.domain.model.addcharacter.CharacterInput;
import com.duckervn.movieservice.service.ICharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/characters")
@RequiredArgsConstructor
public class CharacterController {
    private final ICharacterService characterService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid CharacterInput characterInput) {
        Response response = Response.builder().code(HttpStatus.CREATED.value()).message(RespMessage.CREATED_CHARACTER)
                .result(characterService.save(characterInput)).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PatchMapping("/{characterId}")
    public ResponseEntity<?> update(@PathVariable Long characterId, @RequestBody @Valid CharacterInput characterInput) {
        Response response = Response.builder()
                .code(HttpStatus.OK.value())
                .result(characterService.update(characterId, characterInput))
                .message(RespMessage.UPDATED_CHARACTER).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{characterId}")
    public ResponseEntity<?> delete(@PathVariable Long characterId) {
        characterService.delete(characterId);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_CHARACTER).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam List<Long> characterIds) {
        characterService.delete(characterIds);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_CHARACTERS).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(required = false) String name) {
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.FOUND_ALL_CHARACTERS)
                .results(characterService.findAll(name)).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(characterService.findById(id));
    }
}
