package com.duckervn.movieservice.service;

import com.duckervn.movieservice.domain.entity.Character;
import com.duckervn.movieservice.domain.model.addcharacter.CharacterInput;

import java.util.List;

public interface ICharacterService {

    Character findById(Long id);

    Character save(Character character);

    Character save(CharacterInput characterInput);

    List<Character> findAll(String name);

    Character update(Long characterId, CharacterInput characterInput);

    void delete(Long characterId);

    void delete(List<Long> characterIds);
}
