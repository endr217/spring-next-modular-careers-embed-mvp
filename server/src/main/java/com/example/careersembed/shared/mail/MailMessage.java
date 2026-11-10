package com.example.careersembed.shared.mail;

public record MailMessage(
    String toEmail, String toName, String subject, String textContent, String htmlContent) {}
