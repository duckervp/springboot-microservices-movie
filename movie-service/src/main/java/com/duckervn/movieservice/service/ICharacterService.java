package com.duckervn.movieservice.service;

import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.domain.entity.Character;
import com.duckervn.movieservice.domain.model.addcharacter.CharacterInput;
import com.duckervn.movieservice.domain.model.addgenre.GenreInput;

import java.util.List;

public interface ICharacterService {
    Response findCharacter(Long id);

    Character findById(Long id);

    Response save(Character character);

    Response save(CharacterInput characterInput);

    Response findAll();

    Response update(Long characterId, CharacterInput characterInput);

    Response delete(Long characterId);

    Response delete(List<Long> characterIds);
}
