"use client";

import { useTranslations } from "next-intl";
import { Button } from "@/shared/components/ui/button";

export default function Error({ reset }: { reset: () => void }) {
  const t = useTranslations("errors");

  return (
    <div className="flex flex-col items-start gap-3">
      <h1 className="text-xl font-semibold">{t("title")}</h1>
      <p className="text-muted-foreground">{t("description")}</p>
      <Button variant="outline" onClick={reset}>
        {t("retry")}
      </Button>
    </div>
  );
}
