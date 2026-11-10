export type WebsiteTheme = "light" | "dark";

export type Website = {
  id: number;
  name: string;
  domain: string;
  slug: string;
  logo_url: string | null;
  branding_color: string | null;
  font_family: string | null;
  theme: WebsiteTheme;
  created_at: string;
};

export type WebsiteDetails = Website & {
  company: { id: number; name: string; slug: string };
  job_postings_count: number;
};
