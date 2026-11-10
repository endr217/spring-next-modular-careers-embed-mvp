package com.example.careersembed.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Public URLs of the application.
 *
 * @param publicUrl base URL the API is reachable at from customer websites (used in embed code)
 * @param frontendUrl base URL of the dashboard client (used in e-mails and payment redirects)
 * @param name product name shown in e-mails
 */
@ConfigurationProperties(prefix = "app")
public record AppProperties(String publicUrl, String frontendUrl, String name) {}
