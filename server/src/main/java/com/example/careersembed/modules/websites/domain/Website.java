package com.example.careersembed.modules.websites.domain;

import com.example.careersembed.modules.companies.domain.Company;
import com.example.careersembed.shared.domain.AuditedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/** A company website the careers widget is embedded on. */
@Entity
@Table(name = "websites")
public class Website extends AuditedEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "company_id")
  private Company company;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String domain;

  @Column(nullable = false, unique = true)
  private String slug;

  @Column(name = "logo_path")
  private String logoPath;

  @Column(name = "branding_color")
  private String brandingColor;

  @Column(name = "font_family")
  private String fontFamily;

  @Column(nullable = false)
  private WebsiteTheme theme = WebsiteTheme.LIGHT;

  protected Website() {}

  public Website(
      Company company,
      String name,
      String domain,
      String slug,
      String brandingColor,
      String fontFamily,
      WebsiteTheme theme) {
    this.company = company;
    this.name = name;
    this.domain = domain;
    this.slug = slug;
    this.brandingColor = brandingColor;
    this.fontFamily = fontFamily;
    this.theme = theme;
  }

  public Company getCompany() {
    return company;
  }

  public String getName() {
    return name;
  }

  public String getDomain() {
    return domain;
  }

  public String getSlug() {
    return slug;
  }

  public String getLogoPath() {
    return logoPath;
  }

  public String getBrandingColor() {
    return brandingColor;
  }

  public String getFontFamily() {
    return fontFamily;
  }

  public WebsiteTheme getTheme() {
    return theme;
  }
}
