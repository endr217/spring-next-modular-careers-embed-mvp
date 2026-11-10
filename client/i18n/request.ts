import { getRequestConfig } from "next-intl/server";
import { timeZone } from "./config";
import { resolveLocale } from "./resolve-locale";

export default getRequestConfig(async () => {
  const locale = await resolveLocale();

  return {
    locale,
    timeZone,
    messages: (await import(`../messages/${locale}.json`)).default,
  };
});
