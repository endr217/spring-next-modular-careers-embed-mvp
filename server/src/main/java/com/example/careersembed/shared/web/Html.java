package com.example.careersembed.shared.web;

import org.springframework.web.util.HtmlUtils;

/** HTML escaping for values written into e-mails and embed markup. */
public final class Html {

  private Html() {}

  public static String escape(String value) {
    return value == null ? "" : HtmlUtils.htmlEscape(value);
  }
}
