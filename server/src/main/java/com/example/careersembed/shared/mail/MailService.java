package com.example.careersembed.shared.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/** Sends HTML e-mails over SMTP in the background, retrying a few times on failure. */
@Service
public class MailService {

  private static final Logger log = LoggerFactory.getLogger(MailService.class);
  private static final int MAX_ATTEMPTS = 3;
  private static final long RETRY_DELAY_MILLIS = 30_000;

  private final JavaMailSender mailSender;
  private final MailProperties properties;

  public MailService(JavaMailSender mailSender, MailProperties properties) {
    this.mailSender = mailSender;
    this.properties = properties;
  }

  @Async
  public void send(MailMessage message) {
    for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
      try {
        mailSender.send(toMime(message));
        log.info("E-mail \"{}\" sent", message.subject());
        return;
      } catch (MailException | MessagingException | UnsupportedEncodingException ex) {
        log.warn("E-mail \"{}\" attempt {} failed: {}", message.subject(), attempt, ex.getMessage());
        if (attempt < MAX_ATTEMPTS && !sleep()) {
          return;
        }
      }
    }
    log.error("E-mail \"{}\" could not be sent", message.subject());
  }

  private MimeMessage toMime(MailMessage message)
      throws MessagingException, UnsupportedEncodingException {
    MimeMessage mime = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mime, true, StandardCharsets.UTF_8.name());
    helper.setFrom(properties.fromAddress(), properties.fromName());
    helper.setTo(new jakarta.mail.internet.InternetAddress(message.toEmail(), message.toName()));
    helper.setSubject(message.subject());
    helper.setText(message.textContent(), message.htmlContent());
    return mime;
  }

  private static boolean sleep() {
    try {
      Thread.sleep(RETRY_DELAY_MILLIS);
      return true;
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
      return false;
    }
  }
}
