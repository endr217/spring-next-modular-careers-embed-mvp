import { useTranslations } from "next-intl";

export function LoadingText() {
  const t = useTranslations("common");
  return <p className="text-sm text-muted-foreground">{t("loading")}</p>;
}
