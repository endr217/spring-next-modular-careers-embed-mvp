package com.example.careersembed.modules.websites.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Locale;

public enum WebsiteTheme {
  LIGHT,
  DARK;

  public String value() {
    return name().toLowerCase(Locale.ROOT);
  }

  public static WebsiteTheme fromValue(String value) {
    return value == null || value.isBlank()
        ? LIGHT
        : WebsiteTheme.valueOf(value.toUpperCase(Locale.ROOT));
  }

  @Converter(autoApply = true)
  public static class DbConverter implements AttributeConverter<WebsiteTheme, String> {

    @Override
    public String convertToDatabaseColumn(WebsiteTheme theme) {
      return theme == null ? null : theme.value();
    }

    @Override
    public WebsiteTheme convertToEntityAttribute(String value) {
      return value == null ? null : WebsiteTheme.fromValue(value);
    }
  }
}
