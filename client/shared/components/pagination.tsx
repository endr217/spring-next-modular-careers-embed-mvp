import Link from "next/link";
import { getTranslations } from "next-intl/server";
import { Button } from "@/shared/components/ui/button";
import type { Page } from "@/shared/types";

type Props = {
  page: Pick<Page<unknown>, "current_page" | "last_page" | "total">;
  basePath: string;
};

export async function Pagination({ page, basePath }: Props) {
  if (page.last_page <= 1) return null;
  const t = await getTranslations("common.pagination");
  const current = page.current_page;
  const href = (index: number) => `${basePath}?${new URLSearchParams({ page: String(index) })}`;

  return (
    <nav className="mt-4 flex items-center justify-between text-sm text-muted-foreground" aria-label={t("label")}>
      <span>{t("summary", { page: current, totalPages: page.last_page, total: page.total })}</span>
      <div className="flex gap-2">
        <Button asChild variant="outline" size="sm" aria-disabled={current <= 1}>
          {current <= 1 ? <span>{t("previous")}</span> : <Link href={href(current - 1)}>{t("previous")}</Link>}
        </Button>
        <Button asChild variant="outline" size="sm" aria-disabled={current >= page.last_page}>
          {current >= page.last_page ? (
            <span>{t("next")}</span>
          ) : (
            <Link href={href(current + 1)}>{t("next")}</Link>
          )}
        </Button>
      </div>
    </nav>
  );
}
