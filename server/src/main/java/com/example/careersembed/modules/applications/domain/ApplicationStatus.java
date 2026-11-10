package com.example.careersembed.modules.applications.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Locale;

public enum ApplicationStatus {
  NEW,
  REVIEWED,
  ACCEPTED,
  REJECTED;

  public String value() {
    return name().toLowerCase(Locale.ROOT);
  }

  @Converter(autoApply = true)
  public static class DbConverter implements AttributeConverter<ApplicationStatus, String> {

    @Override
    public String convertToDatabaseColumn(ApplicationStatus status) {
      return status == null ? null : status.value();
    }

    @Override
    public ApplicationStatus convertToEntityAttribute(String value) {
      return value == null ? null : ApplicationStatus.valueOf(value.toUpperCase(Locale.ROOT));
    }
  }
}
