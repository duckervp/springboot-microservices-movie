package com.duckervn.authservice.config;

import com.duckervn.authservice.common.Utils;
import com.duckervn.authservice.domain.entity.Client;
import com.duckervn.authservice.domain.entity.User;
import com.duckervn.authservice.domain.model.gettoken.TokenOutput;
import com.duckervn.authservice.domain.model.register.RegisterInput;
import com.duckervn.authservice.repository.ClientRepository;
import com.duckervn.authservice.repository.UserRepository;
import com.duckervn.authservice.service.IUserService;
import com.duckervn.authservice.validation.validator.EmailValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final ClientRepository clientRepository;

    private final ServiceConfig serviceConfig;

    private final IUserService userService;

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> userInfo = user.getAttributes();

        String name = (String) userInfo.get("name");
        String email = (String) userInfo.get("email");
        String imageUrl = (String) userInfo.get("picture");
        if (Objects.isNull(imageUrl)) {
            imageUrl = (String) userInfo.get("avatar_url");
        }

        // Create UriComponentsBuilder
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();

        builder.uri(URI.create(String.format("%s/login/oauth2/redirect", serviceConfig.getFrontendGateway())));

        EmailValidator emailValidator = new EmailValidator();
        if (StringUtils.isNotBlank(email) && emailValidator.isValid(email, null)) {
            String password = Utils.genSecret(email);
            TokenOutput tokenOutput;
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                String uniqueId = userOptional.get().getId();
                Optional<Client> clientOptional = clientRepository.findById(uniqueId);
                if (clientOptional.isPresent()) {
                    Client client = clientOptional.get();
                    String oldClientSecret = client.getClientSecret();
                    userService.updatePassword(client, password, true);
                    tokenOutput = userService.login(client.getClientId(), password);
                    userService.updatePassword(client, oldClientSecret, false);
                } else {
                    tokenOutput = new TokenOutput();
                }
            } else {
                tokenOutput = userService.register(
                        RegisterInput.builder()
                                .name(name)
                                .email(email)
                                .avatarUrl(imageUrl)
                                .username(email)
                                .password(password)
                                .build());
            }

            // Add query parameters
            if (StringUtils.isNotBlank(tokenOutput.getAccess_token())) {
                builder.queryParam("token", tokenOutput.getAccess_token());
            }
        } else {
            // Add query parameters
            builder.queryParam("error", "Invalid email or cannot obtain email from your social account!");
        }

        // Build the URI with query parameters
        String uriString = builder.build().toUriString();

        response.sendRedirect(uriString);
    }
}
