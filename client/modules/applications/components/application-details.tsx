import { getTranslations } from "next-intl/server";
import { DateTime } from "@/shared/components/date-time";
import { DeleteButton } from "@/shared/components/delete-button";
import { LinkButton } from "@/shared/components/link-button";
import { PageHeader } from "@/shared/components/page-header";
import { Card, CardContent } from "@/shared/components/ui/card";
import { deleteApplication } from "../actions/delete-application";
import { getApplication } from "../queries/get-applications";
import { StatusBadge } from "./status-badge";

export async function ApplicationDetails({ id }: { id: string }) {
  const [t, application] = await Promise.all([getTranslations("applications"), getApplication(id)]);
  const backHref = `/jobs/${application.job_posting_id}/applications`;

  return (
    <>
      <PageHeader title={application.name}>
        <LinkButton href={backHref} variant="outline">
          {t("back")}
        </LinkButton>
        <DeleteButton
          action={deleteApplication.bind(null, application.id, application.job_posting_id)}
          confirmText={t("deleteConfirm", { name: application.name })}
        />
      </PageHeader>
      <Card>
        <CardContent className="grid gap-4 text-sm">
          <dl className="grid gap-x-6 gap-y-2 sm:grid-cols-[max-content_1fr]">
            <dt className="text-muted-foreground">{t("fields.job_posting")}</dt>
            <dd className="font-medium">{application.job_posting.title}</dd>
            <dt className="text-muted-foreground">{t("fields.email")}</dt>
            <dd>
              <a href={`mailto:${application.email}`} className="underline">
                {application.email}
              </a>
            </dd>
            <dt className="text-muted-foreground">{t("fields.status")}</dt>
            <dd>
              <StatusBadge status={application.status} />
            </dd>
            <dt className="text-muted-foreground">{t("fields.submitted_at")}</dt>
            <dd>
              <DateTime value={application.submitted_at} />
            </dd>
            <dt className="text-muted-foreground">{t("fields.cv_file_path")}</dt>
            <dd className="break-all">{application.cv_file_path}</dd>
          </dl>
          <div>
            <h2 className="mb-1 font-medium">{t("fields.motivation_text")}</h2>
            <p className="whitespace-pre-line text-muted-foreground">
              {application.motivation_text ?? t("noMotivation")}
            </p>
          </div>
        </CardContent>
      </Card>
    </>
  );
}
