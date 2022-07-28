package com.duckervn.authservice.service.impl;

import com.duckervn.authservice.common.Credential;
import com.duckervn.authservice.service.IUserService;
import com.duckervn.authservice.common.Scope;
import com.duckervn.authservice.domain.entity.User;
import com.duckervn.authservice.domain.model.register.RegisterInput;
import com.duckervn.authservice.repository.UserRepository;
import com.duckervn.authservice.service.JpaRegisteredClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final JpaRegisteredClientRepository jpaRegisteredClientRepository;

    private final TokenSettings tokenSettings;

    private final PasswordEncoder passwordEncoder;

    /**
     * @param registerInput register info
     */
    @Override
    public Map<String, String> register(RegisterInput registerInput) {
        if (userRepository.existsById(registerInput.getUsername())) {
            throw new IllegalArgumentException("Username is already taken: " + registerInput.getUsername());
        }
        if (userRepository.existsByEmail(registerInput.getEmail())) {
            throw new IllegalArgumentException("Email is already exist: " + registerInput.getEmail());
        }

        User user = objectMapper.convertValue(registerInput, User.class);
        user.setId(registerInput.getUsername());
        if (Objects.nonNull(registerInput.getDobs())) {
            user.setDob(convertStringToLocalDate(registerInput.getDobs()));
        }

        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(user.getId())
                .clientSecret(passwordEncoder.encode(registerInput.getPassword()))
                .scope(Scope.USER.toString())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .clientName(Objects.nonNull(user.getName()) ? user.getName() : user.getId())
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .tokenSettings(tokenSettings)
                .build();

        jpaRegisteredClientRepository.save(registeredClient);
        userRepository.save(user);
        Map<String, String> userCredentials = new HashMap<>();
        userCredentials.put(Credential.CLIENT_ID, registerInput.getUsername());
        userCredentials.put(Credential.CLIENT_SECRET, registerInput.getPassword());
        return userCredentials;
    }

    /**
     * Convert String LocalDate to Object
     * @param localDateString string of date
     * @return LocalDate object
     */
    private LocalDate convertStringToLocalDate(String localDateString) {
        List<Integer> dobs = Arrays.stream(localDateString.split("[/-:]"))
                .map(Integer::parseInt).collect(Collectors.toList());
        return LocalDate.of(dobs.get(2), dobs.get(1), dobs.get(0));
    }
}
