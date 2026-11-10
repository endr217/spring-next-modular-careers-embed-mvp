import "server-only";
import { cookies, headers } from "next/headers";
import { defaultLocale, localeCookie, locales, type Locale } from "./config";

const isLocale = (value: string | undefined): value is Locale =>
  locales.includes(value as Locale);

function fromAcceptLanguage(header: string | null) {
  const tags = (header ?? "").split(",").map((part) => part.split(";")[0].trim().toLowerCase());
  return tags.map((tag) => tag.split("-")[0]).find(isLocale);
}

export async function resolveLocale(): Promise<Locale> {
  if ((locales as readonly string[]).length === 1) return defaultLocale;
  const cookie = (await cookies()).get(localeCookie)?.value;
  if (isLocale(cookie)) return cookie;
  return fromAcceptLanguage((await headers()).get("accept-language")) ?? defaultLocale;
}
