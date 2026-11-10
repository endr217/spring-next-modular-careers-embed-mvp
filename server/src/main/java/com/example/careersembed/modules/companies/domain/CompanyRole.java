package com.example.careersembed.modules.companies.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Locale;

public enum CompanyRole {
  OWNER,
  ADMIN,
  VIEWER;

  public String value() {
    return name().toLowerCase(Locale.ROOT);
  }

  @Converter(autoApply = true)
  public static class DbConverter implements AttributeConverter<CompanyRole, String> {

    @Override
    public String convertToDatabaseColumn(CompanyRole role) {
      return role == null ? null : role.value();
    }

    @Override
    public CompanyRole convertToEntityAttribute(String value) {
      return value == null ? null : CompanyRole.valueOf(value.toUpperCase(Locale.ROOT));
    }
  }
}
