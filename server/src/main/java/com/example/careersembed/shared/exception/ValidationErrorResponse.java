package com.example.careersembed.shared.exception;

import java.util.List;
import java.util.Map;

/** Validation error body: a summary message plus messages per field. */
public record ValidationErrorResponse(String message, Map<String, List<String>> errors) {}
