package com.duckervn.movieservice.service.impl;

import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.common.RespMessage;
import com.duckervn.movieservice.domain.entity.Character;
import com.duckervn.movieservice.domain.exception.ResourceNotFoundException;
import com.duckervn.movieservice.domain.model.addcharacter.CharacterInput;
import com.duckervn.movieservice.repository.CharacterRepository;
import com.duckervn.movieservice.repository.MovieCharacterRepository;
import com.duckervn.movieservice.service.ICharacterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class CharacterService implements ICharacterService {
    private final CharacterRepository characterRepository;

    private final MovieCharacterRepository movieCharacterRepository;

    private final ObjectMapper objectMapper;

    @Override
    public Character findById(Long id) {
        return characterRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public Character save(Character character) {
        character.setCreatedAt(LocalDateTime.now());
        character.setModifiedAt(LocalDateTime.now());
        characterRepository.save(character);
        return character;
    }

    @Override
    public Character save(CharacterInput characterInput) {
        Character character = objectMapper.convertValue(characterInput, Character.class);
        return save(character);
    }

    /**
     * @return list character
     */
    @Override
    public List<Character> findAll(String name) {
        if (Objects.nonNull(name)) {
            name = "%" + name + "%";
        }
        return characterRepository.findAll(name);
    }

    @Override
    public Character update(Long characterId, CharacterInput characterInput) {
        Character character = findById(characterId);

        if (Objects.nonNull(characterInput.getName())) {
            character.setName(characterInput.getName());
        }

        if (Objects.nonNull(characterInput.getAvatarUrl())) {
            character.setAvatarUrl(characterInput.getAvatarUrl());
        }

        if (Objects.nonNull(characterInput.getDescription())) {
            character.setDescription(characterInput.getDescription());
        }

        character.setModifiedAt(LocalDateTime.now());

        characterRepository.save(character);

        return character;
    }

    @Override
    public void delete(Long characterId) {
        Character character = findById(characterId);

        movieCharacterRepository.deleteByCharacterId(characterId);

        characterRepository.delete(character);
    }

    @Override
    public void delete(List<Long> characterIds) {
        List<Character> characters = characterRepository.findAllById(characterIds);

        movieCharacterRepository.deleteByCharacterIdIn(characterIds);

        characterRepository.deleteAll(characters);
    }
}
