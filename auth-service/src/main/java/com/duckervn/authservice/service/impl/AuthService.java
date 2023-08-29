package com.duckervn.authservice.service.impl;

import com.duckervn.authservice.common.*;
import com.duckervn.authservice.config.CustomOAuth2AccessTokenProvider;
import com.duckervn.authservice.config.CustomOAuth2RefreshTokenProvider;
import com.duckervn.authservice.config.ServiceConfig;
import com.duckervn.authservice.domain.entity.Client;
import com.duckervn.authservice.domain.entity.ResetPasswordToken;
import com.duckervn.authservice.domain.entity.User;
import com.duckervn.authservice.domain.exception.InvalidTokenException;
import com.duckervn.authservice.domain.exception.ResourceNotFoundException;
import com.duckervn.authservice.domain.model.addcampaignrecipient.CampaignRecipientInput;
import com.duckervn.authservice.domain.model.changepassword.ChangePasswordInput;
import com.duckervn.authservice.domain.model.gettoken.TokenOutput;
import com.duckervn.authservice.domain.model.register.RegisterInput;
import com.duckervn.authservice.domain.model.resetpassword.ResetPasswordInput;
import com.duckervn.authservice.queue.EventProducer;
import com.duckervn.authservice.repository.ClientRepository;
import com.duckervn.authservice.repository.UserRepository;
import com.duckervn.authservice.service.IAuthService;
import com.duckervn.authservice.service.IResetPasswordTokenService;
import com.duckervn.authservice.service.JpaRegisteredClientRepository;
import com.duckervn.authservice.service.client.OAuth2AuthorizationClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
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

    private final CustomOAuth2RefreshTokenProvider customOAuth2RefreshTokenProvider;

    private final CustomOAuth2AccessTokenProvider customOAuth2AccessTokenProvider;

    @Override
    public void logout(String refreshToken) {
        customOAuth2RefreshTokenProvider.remove(refreshToken);
    }

    @Override
    public TokenOutput refresh(String refreshToken) {
        OAuth2AccessToken newAccessToken = customOAuth2RefreshTokenProvider.refresh(refreshToken, jpaRegisteredClientRepository, customOAuth2AccessTokenProvider);

        return TokenOutput.builder()
                .access_token(newAccessToken.getTokenValue())
                .expires_in(Math.abs(Duration.between(Objects.requireNonNull(newAccessToken.getExpiresAt()), Instant.now()).getSeconds()))
                .scope(String.join(" ", newAccessToken.getScopes()).strip())
                .token_type(newAccessToken.getTokenType().getValue())
                .build();
    }

    @Override
    public TokenOutput login(String clientId, String clientSecret) {
        TokenOutput tokenOutput = authorizationClient.getToken(
                Credential.GRANT_TYPE,
                clientId,
                clientSecret
        );

        if (Objects.nonNull(tokenOutput) && StringUtils.isNotBlank(tokenOutput.getAccess_token())) {
            RegisteredClient registeredClient = jpaRegisteredClientRepository.findByClientId(clientId);
            OAuth2RefreshToken generatedRefreshToken = customOAuth2RefreshTokenProvider.authenticate(registeredClient);
            tokenOutput.setRefresh_token(generatedRefreshToken.getTokenValue());
        }

        return tokenOutput;
    }

    /**
     * @param registerInput register info
     */
    @Override
    public TokenOutput register(RegisterInput registerInput) {
        if (userRepository.existsByEmail(registerInput.getEmail())) {
            throw new IllegalArgumentException("Email is already exist: " + registerInput.getEmail());
        }

        String uniqueId = UUID.randomUUID().toString();

        User user = objectMapper.convertValue(registerInput, User.class);
        user.setId(uniqueId);
        user.setExp(0L);
        user.setLevel(0);
        user.setTitle("New Member");
        user.setCreatedAt(LocalDateTime.now());

        if (Objects.nonNull(registerInput.getBirthdate())) {
            user.setDob(Utils.convertStringToLocalDate(registerInput.getBirthdate()));
        }

        RegisteredClient registeredClient = RegisteredClient.withId(uniqueId)
                .clientId(registerInput.getEmail())
                .clientSecret(passwordEncoder.encode(registerInput.getPassword()))
                .scope(Scope.USER.toString())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .clientName(Objects.nonNull(user.getName()) ? user.getName() : registerInput.getEmail())
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .tokenSettings(tokenSettings)
                .build();

        jpaRegisteredClientRepository.save(registeredClient);

        userRepository.save(user);

        return login(registerInput.getEmail(), registerInput.getPassword());
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
                .recipientId(client.getId())
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
        return clientRepository.findById(user.getId()).orElseThrow(ResourceNotFoundException::new);
    }
}
