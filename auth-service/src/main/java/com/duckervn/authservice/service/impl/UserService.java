package com.duckervn.authservice.service.impl;

import com.duckervn.authservice.common.*;
import com.duckervn.authservice.config.ServiceConfig;
import com.duckervn.authservice.domain.entity.Client;
import com.duckervn.authservice.domain.entity.Gender;
import com.duckervn.authservice.domain.entity.ResetPasswordToken;
import com.duckervn.authservice.domain.entity.User;
import com.duckervn.authservice.domain.exception.InvalidTokenException;
import com.duckervn.authservice.domain.exception.ResourceNotFoundException;
import com.duckervn.authservice.domain.model.addcampaignrecipient.CampaignRecipientInput;
import com.duckervn.authservice.domain.model.changepassword.ChangePasswordInput;
import com.duckervn.authservice.domain.model.gettoken.TokenOutput;
import com.duckervn.authservice.domain.model.register.RegisterInput;
import com.duckervn.authservice.domain.model.resetpassword.ResetPasswordInput;
import com.duckervn.authservice.domain.model.updateuser.UpdateUserInput;
import com.duckervn.authservice.queue.EventProducer;
import com.duckervn.authservice.repository.ClientRepository;
import com.duckervn.authservice.repository.UserRepository;
import com.duckervn.authservice.service.IResetPasswordTokenService;
import com.duckervn.authservice.service.IUserService;
import com.duckervn.authservice.service.JpaRegisteredClientRepository;
import com.duckervn.authservice.service.client.OAuth2AuthorizationClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final JpaRegisteredClientRepository jpaRegisteredClientRepository;

    private final ClientRepository clientRepository;

    private final TokenSettings tokenSettings;

    private final PasswordEncoder passwordEncoder;

    private final OAuth2AuthorizationClient authorizationClient;

    private final ServiceConfig serviceConfig;

    private final IResetPasswordTokenService resetPasswordTokenService;

    private final EventProducer eventProducer;

    @Override
    public TokenOutput login(String clientId, String clientSecret) {
        return authorizationClient.getToken(
                Credential.GRANT_TYPE,
                clientId,
                clientSecret
        );
    }

    /**
     * @param registerInput register info
     */
    @Override
    public TokenOutput register(RegisterInput registerInput) {
        if (userRepository.existsById(registerInput.getUsername())) {
            throw new IllegalArgumentException("Username is already taken: " + registerInput.getUsername());
        }
        if (userRepository.existsByEmail(registerInput.getEmail())) {
            throw new IllegalArgumentException("Email is already exist: " + registerInput.getEmail());
        }

        User user = objectMapper.convertValue(registerInput, User.class);
        user.setId(registerInput.getUsername());
        user.setCreatedAt(LocalDateTime.now());
        if (Objects.nonNull(registerInput.getBirthdate())) {
            user.setDob(convertStringToLocalDate(registerInput.getBirthdate()));
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

        return login(registerInput.getUsername(), registerInput.getPassword());
    }

    /**
     * Convert String LocalDate to Object
     *
     * @param localDateString string of date
     * @return LocalDate object
     */
    private LocalDate convertStringToLocalDate(String localDateString) {
        List<Integer> dobs = Arrays.stream(localDateString.split("[/-:]"))
                .map(Integer::parseInt).collect(Collectors.toList());
        return LocalDate.of(dobs.get(2), dobs.get(1), dobs.get(0));
    }

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

        if (Objects.nonNull(input.getEmail())) {
            user.setEmail(input.getEmail());
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
            user.setDob(convertStringToLocalDate(input.getBirthdate()));
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
    public void updatePassword(Client client, String newPassword, boolean encode) {
        String password = encode ? passwordEncoder.encode(newPassword) : newPassword;
        client.setClientSecret(password);
        clientRepository.save(client);
    }

    @Override
    public void changePassword(ChangePasswordInput changePasswordInput) {
        Client client = findClientByEmail(changePasswordInput.getEmail());
        if (!passwordEncoder.matches(changePasswordInput.getOldPassword(), client.getClientSecret())) {
            throw new IllegalArgumentException(RespMessage.OLD_PASSWORD_NOT_MATCH);
        }
        updatePassword(client, changePasswordInput.getNewPassword(), true);
    }

    @SneakyThrows
    @Override
    public void requestResetPassword(String email) {
        Client client = findClientByEmail(email);

        ResetPasswordToken resetPasswordToken = resetPasswordTokenService.generateRPT(client.getClientId());

        Map<String, Object> params = new HashMap<>();
        params.put(Constants.RESET_PASSWORD_LINK, serviceConfig.getFrontendGateway().concat("/reset-password")
                .concat("?token=").concat(resetPasswordToken.getToken()));

        CampaignRecipientInput campaignRecipientInput = CampaignRecipientInput.builder()
                .recipientId(client.getClientId())
                .status(Constants.WAITING)
                .retry(0)
                .fixedParams(objectMapper.writeValueAsString(params))
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("campaignId", serviceConfig.getRptCampaignId());
        data.put("campaignRecipientInput", campaignRecipientInput);
        eventProducer.publish(serviceConfig.getCampaignTopic(), serviceConfig.getAddCampaignRecipientEvent(), data);
    }

    @Override
    public void resetPassword(String token, ResetPasswordInput resetPasswordInput) {
        String id = Utils.extractBase64Token(token);

        ResetPasswordToken resetPasswordToken = resetPasswordTokenService.getById(id);

        if (Objects.isNull(resetPasswordToken)) {
            throw new InvalidTokenException(RespMessage.INVALID_TOKEN);
        }

        String clientId = resetPasswordToken.getClientId();
        Long expiredAt = resetPasswordToken.getExpiredAt();
        String token1 = resetPasswordToken.getToken();

        if (Objects.nonNull(clientId) && Objects.nonNull(expiredAt) && Objects.nonNull(token1) && token.equals(token1)) {
            Timestamp expiryTime = new Timestamp(expiredAt);

            if (expiryTime.toLocalDateTime().isBefore(LocalDateTime.now())) {
                throw new InvalidTokenException(RespMessage.TOKEN_EXPIRED);
            }

            Optional<Client> clientOptional = clientRepository.findByClientId(clientId);

            if (clientOptional.isEmpty()) {
                throw new InvalidTokenException(RespMessage.INVALID_TOKEN);
            }

            updatePassword(clientOptional.get(), resetPasswordInput.getNewPassword(), true);
        } else {
            throw new InvalidTokenException(RespMessage.INVALID_TOKEN);
        }

        resetPasswordTokenService.remove(resetPasswordToken);
    }

    private Client findClientByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(ResourceNotFoundException::new);
        return clientRepository.findByClientId(user.getId()).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
