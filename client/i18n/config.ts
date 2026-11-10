export const locales = ["en"] as const;
export const defaultLocale: Locale = "en";
export const localeCookie = "locale";
export const timeZone = "UTC";

export type Locale = (typeof locales)[number];
