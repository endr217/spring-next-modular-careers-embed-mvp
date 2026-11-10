import Link from "next/link";
import { getTranslations } from "next-intl/server";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/shared/components/ui/card";
import { cn } from "@/shared/lib/utils";
import { LOAD_STRATEGIES, type LoadStrategy } from "../domain/load-strategy";
import { getEmbedCode } from "../queries/get-embed-code";
import { CopyButton } from "./copy-button";

type Props = { websiteId: number; websiteSlug: string; strategy: LoadStrategy };

/** Embed snippet for a website, with a live preview rendered in a sandboxed frame. */
export async function EmbedCodePanel({ websiteId, websiteSlug, strategy }: Props) {
  const [t, code] = await Promise.all([getTranslations("embed"), getEmbedCode(websiteSlug, strategy)]);

  return (
    <Card>
      <CardHeader>
        <CardTitle>{t("title")}</CardTitle>
        <CardDescription>{t("description")}</CardDescription>
      </CardHeader>
      <CardContent className="grid gap-4">
        <nav className="flex flex-wrap items-center gap-2 text-sm" aria-label={t("strategy")}>
          <span className="text-muted-foreground">{t("strategy")}</span>
          {LOAD_STRATEGIES.map((value) => (
            <Link
              key={value}
              href={`/websites/${websiteId}?loadStrategy=${value}`}
              aria-current={value === strategy ? "true" : undefined}
              className={cn(
                "rounded-md border px-2 py-1",
                value === strategy ? "bg-muted font-medium text-foreground" : "text-muted-foreground",
              )}
            >
              {t(`strategies.${value}`)}
            </Link>
          ))}
        </nav>
        <div className="flex items-start gap-2">
          <pre className="flex-1 overflow-x-auto rounded-lg bg-muted p-3 text-xs">
            <code>{code.embed_code}</code>
          </pre>
          <CopyButton text={code.embed_code} />
        </div>
        <div>
          <h3 className="mb-2 text-sm font-medium">{t("preview")}</h3>
          <iframe
            title={t("preview")}
            srcDoc={code.embed_code}
            sandbox="allow-scripts allow-forms"
            className="h-96 w-full rounded-lg border bg-white"
          />
        </div>
      </CardContent>
    </Card>
  );
}
