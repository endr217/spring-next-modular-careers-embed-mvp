import { useTranslations } from "next-intl";
import { LinkButton } from "@/shared/components/link-button";

export default function NotFound() {
  const t = useTranslations("errors");

  return (
    <div className="flex flex-col items-start gap-3">
      <h1 className="text-xl font-semibold">{t("notFoundTitle")}</h1>
      <p className="text-muted-foreground">{t("notFoundDescription")}</p>
      <LinkButton href="/websites" variant="outline">
        {t("backToWebsites")}
      </LinkButton>
    </div>
  );
}
