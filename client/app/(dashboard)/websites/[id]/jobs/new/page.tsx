import { getTranslations } from "next-intl/server";
import { Suspense } from "react";
import { NewJobPosting } from "@/modules/job-postings/components/new-job-posting";
import { getWebsite } from "@/modules/websites/server";
import { orNotFound } from "@/shared/api/or-not-found";
import { FormCard } from "@/shared/components/form-card";
import { LoadingText } from "@/shared/components/loading-text";

async function NewJob({ params }: Pick<PageProps<"/websites/[id]/jobs/new">, "params">) {
  const { id } = await params;
  const website = await orNotFound(getWebsite(id));
  return <NewJobPosting websiteId={website.id} />;
}

export default async function NewJobPostingPage({ params }: PageProps<"/websites/[id]/jobs/new">) {
  const t = await getTranslations("jobPostings");

  return (
    <FormCard title={t("new")}>
      <Suspense fallback={<LoadingText />}>
        <NewJob params={params} />
      </Suspense>
    </FormCard>
  );
}
