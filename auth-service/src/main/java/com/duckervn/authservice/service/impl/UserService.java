package com.duckervn.authservice.service.impl;

import com.duckervn.authservice.common.Utils;
import com.duckervn.authservice.domain.entity.Gender;
import com.duckervn.authservice.domain.entity.User;
import com.duckervn.authservice.domain.exception.ResourceNotFoundException;
import com.duckervn.authservice.domain.model.updateuser.UpdateUserInput;
import com.duckervn.authservice.repository.ClientRepository;
import com.duckervn.authservice.repository.UserRepository;
import com.duckervn.authservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;

    private final ClientRepository clientRepository;

    /**
     * Find user
     *
     * @param id client id | username
     * @return User
     */
    @Override
    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Update user
     *
     * @param userId user id
     * @param input  update info
     * @return Response
     */
    @Override
    public User updateUser(String userId, UpdateUserInput input) {
        User user = findById(userId);

        if (Objects.nonNull(input.getName())) {
            user.setName(input.getName());
        }

        if (Objects.nonNull(input.getGender())) {
            user.setGender(Gender.valueOf(input.getGender()));
        }

        if (Objects.nonNull(input.getPhoneNumber())) {
            user.setPhoneNumber(input.getPhoneNumber());
        }

        if (Objects.nonNull(input.getAddress())) {
            user.setAddress(input.getAddress());
        }

        if (Objects.nonNull(input.getBirthdate())) {
            user.setDob(Utils.convertStringToLocalDate(input.getBirthdate()));
        }

        if (Objects.nonNull(input.getAvatarUrl())) {
            user.setAvatarUrl(input.getAvatarUrl());
        }

        if (Objects.nonNull(input.getStatus()) && Arrays.asList(0, 1).contains(input.getStatus())) {
            user.setStatus(input.getStatus());
        }

        user.setModifiedAt(LocalDateTime.now());
        userRepository.save(user);
        return user;
    }

    @Override
    public void deleteUser(String userId) {
        User user = findById(userId);
        clientRepository.deleteByClientId(user.getId());
        userRepository.delete(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
