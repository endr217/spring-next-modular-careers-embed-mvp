package com.example.careersembed.security;

import com.example.careersembed.shared.exception.JsonErrorWriter;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http, JsonErrorWriter errorWriter)
      throws Exception {
    AuthenticationEntryPoint unauthenticated =
        (request, response, ex) ->
            errorWriter.write(response, HttpStatus.UNAUTHORIZED, "error.unauthenticated");

    http.cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.dispatcherTypeMatchers(DispatcherType.ERROR)
                    .permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll()
                    .requestMatchers(
                        HttpMethod.POST,
                        "/api/auth/register",
                        "/api/auth/login",
                        "/api/auth/forgot-password",
                        "/api/auth/reset-password",
                        "/api/embeds/applications")
                    .permitAll()
                    .requestMatchers(
                        HttpMethod.GET,
                        "/api/websites/*/embed-code",
                        "/api/embeds/*/view",
                        "/api/embeds/*/script.js")
                    .permitAll()
                    .requestMatchers("/actuator/health")
                    .permitAll()
                    .requestMatchers("/api/**")
                    .authenticated()
                    .anyRequest()
                    .denyAll())
        .oauth2ResourceServer(
            oauth2 ->
                oauth2.jwt(Customizer.withDefaults()).authenticationEntryPoint(unauthenticated))
        .exceptionHandling(
            e ->
                e.authenticationEntryPoint(unauthenticated)
                    .accessDeniedHandler(
                        (request, response, ex) ->
                            errorWriter.write(response, HttpStatus.FORBIDDEN, "error.forbidden")));
    return http.build();
  }
}
