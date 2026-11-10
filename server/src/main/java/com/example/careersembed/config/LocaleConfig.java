package com.example.careersembed.config;

import java.time.Clock;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class LocaleConfig {

  @Bean
  LocaleResolver localeResolver(
      @Value("${app.i18n.default-locale}") Locale defaultLocale,
      @Value("${app.i18n.supported-locales}") List<Locale> supportedLocales) {
    AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
    resolver.setDefaultLocale(defaultLocale);
    resolver.setSupportedLocales(supportedLocales);
    return resolver;
  }

  @Bean
  Clock clock() {
    return Clock.systemUTC();
  }
}
