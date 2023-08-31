package com.duckervn.authservice.config;

import com.duckervn.authservice.resolver.AuthInfoArgumentResolver;
import com.duckervn.authservice.resolver.UserInfoArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final UserInfoArgumentResolver userInfoArgumentResolver;

    private final AuthInfoArgumentResolver authInfoArgumentResolver;
    /**
     * @param resolvers list handle method argument resolver
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userInfoArgumentResolver);
        resolvers.add(authInfoArgumentResolver);
    }
}
