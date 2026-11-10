package com.example.careersembed.modules.embed.domain;

import com.example.careersembed.shared.web.Html;

/** The &lt;script&gt; tag a company pastes into its website. */
public record ScriptTag(String src, String websiteSlug, LoadStrategy strategy) {

  public String toHtml() {
    StringBuilder html =
        new StringBuilder("<script src=\"")
            .append(Html.escape(src))
            .append("\" data-website-slug=\"")
            .append(Html.escape(websiteSlug))
            .append('"');
    if (strategy.rendersAttribute()) {
      html.append(' ').append(strategy.value());
    }
    return html.append("></script>").toString();
  }
}
