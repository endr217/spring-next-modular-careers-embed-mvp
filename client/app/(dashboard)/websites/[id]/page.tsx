import { getTranslations } from "next-intl/server";
import { Suspense } from "react";
import { toLoadStrategy } from "@/modules/embed";
import { EmbedCodePanel } from "@/modules/embed/components/embed-code-panel";
import { JobPostingsList } from "@/modules/job-postings/components/job-postings-list";
import { getWebsite } from "@/modules/websites/server";
import { WebsiteSummary } from "@/modules/websites/components/website-summary";
import { orNotFound } from "@/shared/api/or-not-found";
import { LinkButton } from "@/shared/components/link-button";
import { LoadingText } from "@/shared/components/loading-text";
import { PageHeader } from "@/shared/components/page-header";
import { readPage, readParam } from "@/shared/lib/search-params";

async function WebsiteView({ params, searchParams }: PageProps<"/websites/[id]">) {
  const [{ id }, query, t] = await Promise.all([params, searchParams, getTranslations("websites")]);
  const website = await orNotFound(getWebsite(id));

  return (
    <div className="grid gap-6">
      <PageHeader title={website.name}>
        <LinkButton href={`/websites/${website.id}/jobs/new`}>{t("newJob")}</LinkButton>
      </PageHeader>
      <WebsiteSummary website={website} />
      <section className="grid gap-3">
        <h2 className="text-lg font-semibold">{t("jobPostings")}</h2>
        <JobPostingsList websiteId={website.id} page={readPage(query)} />
      </section>
      <EmbedCodePanel
        websiteId={website.id}
        websiteSlug={website.slug}
        strategy={toLoadStrategy(readParam(query, "loadStrategy"))}
      />
    </div>
  );
}

export default function WebsitePage(props: PageProps<"/websites/[id]">) {
  return (
    <Suspense fallback={<LoadingText />}>
      <WebsiteView {...props} />
    </Suspense>
  );
}
