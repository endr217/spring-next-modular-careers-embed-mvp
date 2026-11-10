import { getTranslations } from "next-intl/server";
import { DataTable } from "@/shared/components/data-table";
import { DeleteButton } from "@/shared/components/delete-button";
import { EmptyRow } from "@/shared/components/empty-row";
import { LinkButton } from "@/shared/components/link-button";
import { Pagination } from "@/shared/components/pagination";
import { Badge } from "@/shared/components/ui/badge";
import { TableCell, TableRow } from "@/shared/components/ui/table";
import { deleteJobPosting } from "../actions/delete-job-posting";
import { getJobPostings } from "../queries/get-job-postings";
import type { JobPosting } from "../types";

type Props = { websiteId: number; page: number };

export async function JobPostingsList({ websiteId, page }: Props) {
  const [t, postings] = await Promise.all([getTranslations("jobPostings"), getJobPostings(websiteId, page)]);
  const head = [t("fields.title"), t("fields.location"), t("fields.job_type"), t("salary"), t("status"), ""];

  const salary = (posting: JobPosting) =>
    posting.salary_from === null && posting.salary_to === null
      ? t("noSalary")
      : t("salaryRange", { from: posting.salary_from ?? "?", to: posting.salary_to ?? "?" });

  return (
    <>
      <DataTable head={head}>
        {postings.data.length === 0 && <EmptyRow colSpan={head.length} text={t("empty")} />}
        {postings.data.map((posting) => (
          <TableRow key={posting.id}>
            <TableCell className="px-4 font-medium">{posting.title}</TableCell>
            <TableCell className="px-4">{posting.location}</TableCell>
            <TableCell className="px-4">{t(`fields.jobTypes.${posting.job_type}`)}</TableCell>
            <TableCell className="px-4">{salary(posting)}</TableCell>
            <TableCell className="px-4">
              <Badge variant={posting.is_active ? "default" : "secondary"}>
                {posting.is_active ? t("active") : t("inactive")}
              </Badge>
            </TableCell>
            <TableCell className="flex items-start justify-end gap-1 px-4">
              <LinkButton href={`/jobs/${posting.id}/applications`} variant="ghost" size="sm">
                {t("applications")}
              </LinkButton>
              <LinkButton href={`/jobs/${posting.id}/edit`} variant="ghost" size="sm">
                {t("edit")}
              </LinkButton>
              <DeleteButton
                action={deleteJobPosting.bind(null, posting.id)}
                confirmText={t("deleteConfirm", { title: posting.title })}
              />
            </TableCell>
          </TableRow>
        ))}
      </DataTable>
      <Pagination page={postings} basePath={`/websites/${websiteId}`} />
    </>
  );
}
