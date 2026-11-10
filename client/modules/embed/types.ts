export type EmbedCode = {
  embed_code: string;
  website: {
    name: string;
    slug: string;
    branding_color: string | null;
    logo_path: string | null;
    font_family: string | null;
  };
};
