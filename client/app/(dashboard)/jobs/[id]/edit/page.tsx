import { getTranslations } from "next-intl/server";
import { Suspense } from "react";
import { EditJobPosting } from "@/modules/job-postings/components/edit-job-posting";
import { FormCard } from "@/shared/components/form-card";
import { LoadingText } from "@/shared/components/loading-text";

async function Edit({ params }: Pick<PageProps<"/jobs/[id]/edit">, "params">) {
  const { id } = await params;
  return <EditJobPosting id={id} />;
}

export default async function EditJobPostingPage({ params }: PageProps<"/jobs/[id]/edit">) {
  const t = await getTranslations("jobPostings");

  return (
    <FormCard title={t("edit")}>
      <Suspense fallback={<LoadingText />}>
        <Edit params={params} />
      </Suspense>
    </FormCard>
  );
}
