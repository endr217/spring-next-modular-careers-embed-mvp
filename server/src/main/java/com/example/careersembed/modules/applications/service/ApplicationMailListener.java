package com.example.careersembed.modules.applications.service;

import com.example.careersembed.config.AppProperties;
import com.example.careersembed.modules.applications.api.response.ApplicationResponse;
import com.example.careersembed.shared.mail.MailMessage;
import com.example.careersembed.shared.mail.MailService;
import com.example.careersembed.shared.web.Html;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/** E-mails the company about a new application. */
@Component
public class ApplicationMailListener {

  private final MailService mailService;
  private final MessageSource messages;
  private final AppProperties appProperties;

  public ApplicationMailListener(
      MailService mailService, MessageSource messages, AppProperties appProperties) {
    this.mailService = mailService;
    this.messages = messages;
    this.appProperties = appProperties;
  }

  @TransactionalEventListener
  public void on(ApplicationSubmitted event) {
    ApplicationResponse application = event.application();
    var posting = application.jobPosting();

    String subject = msg("mail.application.subject", posting.title());
    String intro = msg("mail.application.intro", posting.title());
    StringBuilder html =
        new StringBuilder()
            .append("<h1>")
            .append(Html.escape(msg("mail.application.heading")))
            .append("</h1><p>")
            .append(Html.escape(intro))
            .append("</p><h2>")
            .append(Html.escape(msg("mail.application.company")))
            .append(": ")
            .append(Html.escape(posting.companyName()))
            .append("</h2><h3>")
            .append(Html.escape(msg("mail.application.applicant")))
            .append(":</h3><ul>")
            .append(item("mail.application.name", application.name()))
            .append(item("mail.application.email", application.email()))
            .append(item("mail.application.submitted_at", application.submittedAt()))
            .append("</ul>");
    if (application.motivationText() != null && !application.motivationText().isBlank()) {
      html.append("<h3>")
          .append(Html.escape(msg("mail.application.motivation")))
          .append(":</h3><p>")
          .append(Html.escape(application.motivationText()))
          .append("</p>");
    }
    html.append("<p>")
        .append(Html.escape(msg("mail.application.thanks")))
        .append("<br>")
        .append(Html.escape(appProperties.name()))
        .append("</p>");

    String text =
        String.join(
            "\n",
            intro,
            msg("mail.application.name") + ": " + application.name(),
            msg("mail.application.email") + ": " + application.email(),
            msg("mail.application.submitted_at") + ": " + application.submittedAt());

    mailService.send(
        new MailMessage(
            posting.companyEmail(), posting.companyName(), subject, text, html.toString()));
  }

  private String item(String labelKey, String value) {
    return "<li>" + Html.escape(msg(labelKey)) + ": " + Html.escape(value) + "</li>";
  }

  private String msg(String key, Object... args) {
    return messages.getMessage(key, args, LocaleContextHolder.getLocale());
  }
}
