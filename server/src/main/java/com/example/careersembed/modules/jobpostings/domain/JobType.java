package com.example.careersembed.modules.jobpostings.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;

public enum JobType {
  FULL_TIME("full-time"),
  PART_TIME("part-time"),
  FREELANCE("freelance");

  public static final String PATTERN = "^(full-time|part-time|freelance)$";

  private final String value;

  JobType(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }

  public static JobType fromValue(String value) {
    return Arrays.stream(values())
        .filter(type -> type.value.equals(value))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Unknown job type: " + value));
  }

  @Converter(autoApply = true)
  public static class DbConverter implements AttributeConverter<JobType, String> {

    @Override
    public String convertToDatabaseColumn(JobType type) {
      return type == null ? null : type.value();
    }

    @Override
    public JobType convertToEntityAttribute(String value) {
      return value == null ? null : JobType.fromValue(value);
    }
  }
}
