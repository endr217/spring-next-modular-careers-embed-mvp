package com.example.careersembed.modules.applications.service;

import com.example.careersembed.modules.applications.api.response.ApplicationResponse;

/** Published after an application is stored; the company is notified once the transaction commits. */
public record ApplicationSubmitted(ApplicationResponse application) {}
