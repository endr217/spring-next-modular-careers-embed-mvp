import { z } from "zod";

const optional = (max: number) =>
  z
    .string()
    .trim()
    .max(max, "tooLong")
    .transform((value) => value || undefined)
    .optional();

export const THEMES = ["light", "dark"] as const;

export const websiteSchema = z.object({
  name: z.string().trim().min(1, "nameRequired").max(255, "tooLong"),
  domain: z.string().trim().min(1, "domainRequired").max(255, "tooLong"),
  branding_color: z
    .string()
    .trim()
    .regex(/^$|^#[0-9a-fA-F]{6}$/, "colorInvalid")
    .transform((value) => value || undefined)
    .optional(),
  font_family: optional(255),
  theme: z.enum(THEMES, "themeInvalid"),
});
