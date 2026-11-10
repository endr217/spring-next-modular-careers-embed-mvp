package com.example.careersembed.shared.exception;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/** Writes a {"message": "..."} error body outside of Spring MVC (security filters). */
@Component
public class JsonErrorWriter {

  private final MessageSource messageSource;

  public JsonErrorWriter(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  public void write(HttpServletResponse response, HttpStatus status, String messageKey)
      throws IOException {
    String message =
        messageSource.getMessage(messageKey, null, messageKey, LocaleContextHolder.getLocale());
    response.setStatus(status.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.getWriter().write("{\"message\":\"" + escape(message) + "\"}");
  }

  private static String escape(String value) {
    StringBuilder out = new StringBuilder(value.length());
    for (char c : value.toCharArray()) {
      switch (c) {
        case '"' -> out.append("\\\"");
        case '\\' -> out.append("\\\\");
        case '\n' -> out.append("\\n");
        case '\r' -> out.append("\\r");
        case '\t' -> out.append("\\t");
        default -> {
          if (c < 0x20) {
            out.append(String.format("\\u%04x", (int) c));
          } else {
            out.append(c);
          }
        }
      }
    }
    return out.toString();
  }
}
