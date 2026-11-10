package com.example.careersembed.modules.embed.service;

import com.example.careersembed.modules.jobpostings.domain.JobPosting;
import com.example.careersembed.modules.websites.domain.Website;
import com.example.careersembed.shared.web.Html;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/** Renders the embed widget script and the job list markup it loads. */
@Component
public class EmbedRenderer {

  private static final String DEFAULT_HEADING_COLOR = "#333";

  private final EmbedUrls urls;
  private final String widgetTemplate;

  public EmbedRenderer(EmbedUrls urls) {
    this.urls = urls;
    this.widgetTemplate = readTemplate("embed/widget.js");
  }

  public String script(Website website) {
    return widgetTemplate
        .replace("__BRANDING_COLOR__", jsString(website.getBrandingColor()))
        .replace("__FONT_FAMILY__", jsString(website.getFontFamily()))
        .replace("__JOBS_ENDPOINT__", jsString(urls.view(website.getSlug())))
        .replace("__APPLY_ENDPOINT__", jsString(urls.store()));
  }

  public String view(Website website, List<JobPosting> jobs) {
    String color =
        website.getBrandingColor() == null ? DEFAULT_HEADING_COLOR : website.getBrandingColor();
    StringBuilder html = new StringBuilder(4096);
    html.append("<!DOCTYPE html><html><head><meta charset=\"utf-8\">")
        .append("<title>")
        .append(Html.escape(website.getName()))
        .append(" Job Listings</title>")
        .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
        .append("<meta name=\"robots\" content=\"noindex,nofollow\">")
        .append(STYLES)
        .append("</head><body><div class=\"job-embed-container\">")
        .append("<div class=\"text-center\">");
    if (website.getLogoPath() != null) {
      html.append("<img class=\"logo\" src=\"")
          .append(Html.escape(website.getLogoPath()))
          .append("\" alt=\"")
          .append(Html.escape(website.getName()))
          .append(" logo\">");
    }
    html.append("<h1 style=\"color: ")
        .append(Html.escape(color))
        .append(";\">")
        .append(Html.escape(website.getName()))
        .append(" Job Openings</h1></div><div id=\"job-list-container\">");
    if (jobs.isEmpty()) {
      html.append("<p>No current job openings.</p>");
    }
    jobs.forEach(job -> appendJob(html, job));
    html.append("</div>").append(FORM).append("</div></body></html>");
    return html.toString();
  }

  private static void appendJob(StringBuilder html, JobPosting job) {
    html.append("<div class=\"job-card\" id=\"job-")
        .append(job.getId())
        .append("\"><h3>")
        .append(Html.escape(job.getTitle()))
        .append("</h3><p><strong>Location:</strong> ")
        .append(Html.escape(job.getLocation()))
        .append("</p><p><strong>Type:</strong> ")
        .append(Html.escape(job.getJobType().value()))
        .append("</p>");
    if (job.getSalaryFrom() != null || job.getSalaryTo() != null) {
      html.append("<p><strong>Salary:</strong> ");
      if (job.getSalaryFrom() != null) {
        html.append('$').append(job.getSalaryFrom());
      }
      if (job.getSalaryTo() != null) {
        html.append(" - $").append(job.getSalaryTo());
      }
      html.append("</p>");
    }
    html.append("<div class=\"description\">")
        .append(Html.escape(job.getDescription()).replace("\n", "<br>"))
        .append("</div><button type=\"button\" class=\"apply-btn\" data-job-id=\"")
        .append(job.getId())
        .append("\">Apply Now</button></div>");
  }

  /** A JavaScript string literal (or null) that is safe inside a script body. */
  static String jsString(String value) {
    if (value == null) {
      return "null";
    }
    StringBuilder out = new StringBuilder(value.length() + 2).append('"');
    for (char c : value.toCharArray()) {
      switch (c) {
        case '"' -> out.append("\\\"");
        case '\\' -> out.append("\\\\");
        case '\n' -> out.append("\\n");
        case '\r' -> out.append("\\r");
        case '<' -> out.append("\\u003c");
        case '>' -> out.append("\\u003e");
        case '&' -> out.append("\\u0026");
        case ' ' -> out.append("\\u2028");
        case ' ' -> out.append("\\u2029");
        default -> {
          if (c < 0x20) {
            out.append(String.format("\\u%04x", (int) c));
          } else {
            out.append(c);
          }
        }
      }
    }
    return out.append('"').toString();
  }

  private static String readTemplate(String path) {
    try (InputStream in = new ClassPathResource(path).getInputStream()) {
      return new String(in.readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException ex) {
      throw new UncheckedIOException("Cannot read " + path, ex);
    }
  }

  private static final String STYLES =
      """
      <style>
        .job-embed-container { line-height: 1.6; color: #333; }
        .job-embed-container .text-center { text-align: center; margin-bottom: 20px; }
        .job-embed-container .logo { max-height: 50px; margin-bottom: 10px; }
        .job-embed-container .job-card { border: 1px solid #ddd; border-radius: 5px; padding: 15px; margin-bottom: 15px; background-color: #f9f9f9; }
        .job-embed-container .apply-btn { background-color: #4CAF50; color: #fff; padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; }
        .job-embed-container .back-btn { background-color: #f1f1f1; color: #333; padding: 8px 12px; border: none; border-radius: 4px; cursor: pointer; margin-bottom: 15px; }
        .job-embed-container .form-group { margin-bottom: 15px; }
        .job-embed-container .form-group label { display: block; margin-bottom: 5px; }
        .job-embed-container .form-control { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
        .job-embed-container .success-message { color: #4CAF50; padding: 10px; background-color: #e8f5e9; border-radius: 4px; margin-bottom: 15px; }
      </style>
      """;

  private static final String FORM =
      """
      <div id="application-form-container" style="display: none;">
        <button type="button" class="back-btn">&larr; Back to Jobs</button>
        <h2 id="apply-job-title">Apply for: </h2>
        <div id="success-message" class="success-message" style="display: none;">
          Your application has been submitted successfully!
        </div>
        <div id="form-error" class="form-error" role="alert"></div>
        <form id="application-form">
          <input type="hidden" id="job-id" name="job_posting_id" value="">
          <div class="form-group">
            <label for="name">Full Name</label>
            <input type="text" id="name" name="name" class="form-control" maxlength="255" required>
          </div>
          <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" class="form-control" maxlength="255" required>
          </div>
          <div class="form-group">
            <label for="motivation_text">Cover Letter/Motivation</label>
            <textarea id="motivation_text" name="motivation_text" class="form-control" rows="5"></textarea>
          </div>
          <button type="submit" class="apply-btn" id="submit-btn">Submit Application</button>
        </form>
      </div>
      """;
}
