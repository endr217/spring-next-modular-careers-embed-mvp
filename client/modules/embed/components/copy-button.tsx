"use client";

import { useTranslations } from "next-intl";
import { useState } from "react";
import { Button } from "@/shared/components/ui/button";

export function CopyButton({ text }: { text: string }) {
  const t = useTranslations("embed");
  const [copied, setCopied] = useState(false);

  const copy = async () => {
    await navigator.clipboard.writeText(text);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <Button type="button" variant="outline" size="sm" onClick={copy}>
      {copied ? t("copied") : t("copy")}
    </Button>
  );
}
