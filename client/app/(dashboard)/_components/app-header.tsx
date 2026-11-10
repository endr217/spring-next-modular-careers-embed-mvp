import { getTranslations } from "next-intl/server";
import { Suspense } from "react";
import { MainNav } from "@/app/(dashboard)/_components/main-nav";
import { Button } from "@/shared/components/ui/button";

export async function AppHeader() {
  const t = await getTranslations();

  return (
    <header className="border-b bg-background">
      <div className="mx-auto flex max-w-6xl flex-wrap items-center justify-between gap-4 px-4 py-3">
        <span className="font-semibold">{t("app.title")}</span>
        <Suspense>
          <MainNav />
        </Suspense>
        <form action="/logout" method="post">
          <Button type="submit" variant="ghost" size="sm">
            {t("nav.logout")}
          </Button>
        </form>
      </div>
    </header>
  );
}
