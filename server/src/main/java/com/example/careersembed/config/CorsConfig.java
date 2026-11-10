package com.example.careersembed.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

  @Bean
  public CorsConfigurationSource corsConfigurationSource(CorsProperties properties) {
    CorsConfiguration dashboard = new CorsConfiguration();
    dashboard.setAllowedOrigins(properties.allowedOrigins());
    dashboard.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    dashboard.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
    dashboard.setMaxAge(3600L);

    // The embed widget runs on customer websites, so its public endpoints accept any origin.
    CorsConfiguration embed = new CorsConfiguration();
    embed.setAllowedOriginPatterns(List.of("*"));
    embed.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
    embed.setAllowedHeaders(List.of("Content-Type", "Accept"));
    embed.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/embeds/**", embed);
    source.registerCorsConfiguration("/api/websites/*/embed-code", embed);
    source.registerCorsConfiguration("/**", dashboard);
    return source;
  }
}
