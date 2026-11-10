import { getTranslations } from "next-intl/server";
import { Suspense } from "react";
import { WebsitesList } from "@/modules/websites/components/websites-list";
import { LinkButton } from "@/shared/components/link-button";
import { LoadingText } from "@/shared/components/loading-text";
import { PageHeader } from "@/shared/components/page-header";

export default async function WebsitesPage() {
  const t = await getTranslations("websites");

  return (
    <>
      <PageHeader title={t("title")}>
        <LinkButton href="/websites/new">{t("new")}</LinkButton>
      </PageHeader>
      <Suspense fallback={<LoadingText />}>
        <WebsitesList />
      </Suspense>
    </>
  );
}
