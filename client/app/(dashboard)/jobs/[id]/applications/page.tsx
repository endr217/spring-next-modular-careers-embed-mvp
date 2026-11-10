import { getTranslations } from "next-intl/server";
import { Suspense } from "react";
import { ApplicationsList } from "@/modules/applications/components/applications-list";
import { getJobPosting } from "@/modules/job-postings/server";
import { LinkButton } from "@/shared/components/link-button";
import { LoadingText } from "@/shared/components/loading-text";
import { PageHeader } from "@/shared/components/page-header";
import { readPage } from "@/shared/lib/search-params";

async function Applications({ params, searchParams }: PageProps<"/jobs/[id]/applications">) {
  const [{ id }, query, t] = await Promise.all([params, searchParams, getTranslations("applications")]);
  const posting = await getJobPosting(id);

  return (
    <>
      <PageHeader title={t("title", { title: posting.title })}>
        <LinkButton href={`/websites/${posting.website.id}`} variant="outline">
          {t("backToWebsite")}
        </LinkButton>
      </PageHeader>
      <ApplicationsList jobPostingId={id} page={readPage(query)} />
    </>
  );
}

export default function ApplicationsPage(props: PageProps<"/jobs/[id]/applications">) {
  return (
    <Suspense fallback={<LoadingText />}>
      <Applications {...props} />
    </Suspense>
  );
}
