package com.duckervn.movieservice.service.impl;

import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.common.RespMessage;
import com.duckervn.movieservice.common.Utils;
import com.duckervn.movieservice.domain.entity.Character;
import com.duckervn.movieservice.domain.exception.ResourceNotFoundException;
import com.duckervn.movieservice.domain.model.addcharacter.CharacterInput;
import com.duckervn.movieservice.repository.CharacterRepository;
import com.duckervn.movieservice.service.ICharacterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CharacterService implements ICharacterService {
    private final CharacterRepository characterRepository;

    private final ObjectMapper objectMapper;

    /**
     * @param id id
     * @return character
     */
    @Override
    public Response findById(Long id) {
        Character character =  characterRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.FOUND_CHARACTER)
                .result(character).build();
    }

    @Override
    public Response save(Character character) {
        character.setCreatedAt(LocalDateTime.now());
        character.setModifiedAt(LocalDateTime.now());
        characterRepository.save(character);
        return Response.builder().code(HttpStatus.CREATED.value()).message(RespMessage.CREATED_CHARACTER)
                .result(character).build();
    }

    @Override
    public Response save(CharacterInput characterInput) {
        Character character = objectMapper.convertValue(characterInput, Character.class);
        return save(character);
    }

    /**
     * @return list character
     */
    @Override
    public Response findAll() {
        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.FOUND_ALL_CHARACTERS)
                .results(Utils.toObjectList(characterRepository.findAll())).build();
    }
}
