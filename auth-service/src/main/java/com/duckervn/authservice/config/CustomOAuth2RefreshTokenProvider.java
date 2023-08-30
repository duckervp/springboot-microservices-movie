package com.duckervn.authservice.config;

import com.duckervn.authservice.domain.exception.InvalidTokenException;
import com.duckervn.authservice.service.JpaRegisteredClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2RefreshTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2RefreshTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.context.ProviderContext;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CustomOAuth2RefreshTokenProvider {
    public static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    private final ProviderSettings providerSettings;

    private final OAuth2AuthorizationService authorizationService;

    public OAuth2RefreshToken authenticate(RegisteredClient registeredClient) {
        assert registeredClient != null;
        OAuth2ClientAuthenticationToken clientPrincipal = new OAuth2ClientAuthenticationToken(registeredClient, ClientAuthenticationMethod.CLIENT_SECRET_POST, null);

        Set<String> authorizedScopes = registeredClient.getScopes();

        OAuth2RefreshTokenAuthenticationToken refreshTokenAuthentication = new OAuth2RefreshTokenAuthenticationToken("refresh token", clientPrincipal, authorizedScopes, new HashMap<>());

        // @formatter:off
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(clientPrincipal)
                .providerContext(new ProviderContext(providerSettings, null))
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizedScopes(authorizedScopes)
                .authorizationGrant(refreshTokenAuthentication);
        // @formatter:on

        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();

        OAuth2RefreshTokenGenerator tokenGenerator = new OAuth2RefreshTokenGenerator();

        OAuth2RefreshToken generatedRefreshToken = tokenGenerator.generate(tokenContext);
        if (generatedRefreshToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the refresh token.", ERROR_URI);
            throw new OAuth2AuthenticationException(error);
        }

        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(clientPrincipal.getName())
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .attribute(OAuth2Authorization.AUTHORIZED_SCOPE_ATTRIBUTE_NAME, authorizedScopes);

        authorizationBuilder.refreshToken(generatedRefreshToken);

        OAuth2Authorization authorization = authorizationBuilder.build();
        authorizationService.save(authorization);

        return generatedRefreshToken;
    }


    public OAuth2AccessToken refresh(String refreshToken, JpaRegisteredClientRepository jpaRegisteredClientRepository, CustomOAuth2AccessTokenProvider customOAuth2AccessTokenProvider) {
        OAuth2Authorization authorization = this.authorizationService.findByToken(refreshToken, OAuth2TokenType.REFRESH_TOKEN);

        if (authorization == null) {
            throw new InvalidTokenException("Invalid refresh token.");
        }

        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken1 = authorization.getRefreshToken();
        if (Objects.isNull(refreshToken1) || !refreshToken1.isActive()) {
            // As per https://tools.ietf.org/html/rfc6749#section-5.2
            // invalid_grant: The provided authorization grant (e.g., authorization code,
            // resource owner credentials) or refresh token is invalid, expired, revoked [...].
            throw new InvalidTokenException("Invalid refresh token.");
        }

        String registeredClientId = authorization.getRegisteredClientId();
        assert(registeredClientId != null);

        RegisteredClient registeredClient = jpaRegisteredClientRepository.findById(registeredClientId);

        return customOAuth2AccessTokenProvider.authenticate(registeredClient);
    }

    public void remove(String refreshToken) {
        OAuth2Authorization authorization = this.authorizationService.findByToken(refreshToken, OAuth2TokenType.REFRESH_TOKEN);

        if (Objects.nonNull(authorization)) {
            this.authorizationService.remove(authorization);
        }
    }
}
