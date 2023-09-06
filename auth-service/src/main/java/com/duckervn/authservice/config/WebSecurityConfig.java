package com.duckervn.authservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final OAuth2SuccessHandler oauth2SuccessHandler;

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/oauth2/**").permitAll()
                .antMatchers("/users/auth/login").permitAll()
                .antMatchers("/users/auth/register").permitAll()
                .antMatchers("/users/auth/reset-password-request").permitAll()
                .antMatchers("/users/auth/reset-password").permitAll()
                .antMatchers("/users/auth/refresh").permitAll()
                .antMatchers("/users/auth/logout").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt().and().authenticationEntryPoint(authenticationEntryPoint)
                .and().oauth2Login()
                .successHandler(oauth2SuccessHandler)
        ;
        return http.build();
    }

}
