package com.duckervn.movieservice.service;

import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.domain.entity.Character;
import com.duckervn.movieservice.domain.model.addcharacter.CharacterInput;

public interface ICharacterService {
    Response findById(Long id);

    Response save(Character character);

    Response save(CharacterInput characterInput);

    Response findAll();
}
