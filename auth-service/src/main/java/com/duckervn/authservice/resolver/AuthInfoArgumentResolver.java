package com.duckervn.authservice.resolver;

import com.duckervn.authservice.common.Scope;
import com.duckervn.authservice.domain.model.auth.Auth;
import com.duckervn.authservice.resolver.annotation.AuthInfo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthInfoArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Objects.nonNull(parameter.getParameterAnnotation(AuthInfo.class));
    }

    @Override
    public Object resolveArgument(
            @NonNull MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            @NonNull NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        Jwt jwt = (Jwt) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Auth auth = new Auth();
        if (jwt.hasClaim("id")) {
            auth.setUserId(jwt.getClaimAsString("id"));
        }
        if (jwt.hasClaim("scope")) {
            String scope = jwt.getClaimAsString("scope");
            auth.setAdmin(scope.contains(Scope.ADMIN.toString()));
        }
        return auth;
    }
}
