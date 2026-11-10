package com.example.careersembed.shared.mail;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.mail")
public record MailProperties(String fromAddress, String fromName) {}
