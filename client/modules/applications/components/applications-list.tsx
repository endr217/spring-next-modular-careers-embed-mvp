import Link from "next/link";
import { getTranslations } from "next-intl/server";
import { DataTable } from "@/shared/components/data-table";
import { DateTime } from "@/shared/components/date-time";
import { EmptyRow } from "@/shared/components/empty-row";
import { Pagination } from "@/shared/components/pagination";
import { TableCell, TableRow } from "@/shared/components/ui/table";
import { getApplications } from "../queries/get-applications";
import { StatusBadge } from "./status-badge";

type Props = { jobPostingId: string; page: number };

export async function ApplicationsList({ jobPostingId, page }: Props) {
  const [t, applications] = await Promise.all([
    getTranslations("applications"),
    getApplications(jobPostingId, page),
  ]);
  const head = [t("fields.name"), t("fields.email"), t("fields.status"), t("fields.submitted_at")];

  return (
    <>
      <DataTable head={head}>
        {applications.data.length === 0 && <EmptyRow colSpan={head.length} text={t("empty")} />}
        {applications.data.map((application) => (
          <TableRow key={application.id}>
            <TableCell className="px-4 font-medium">
              <Link href={`/applications/${application.id}`} className="underline-offset-4 hover:underline">
                {application.name}
              </Link>
            </TableCell>
            <TableCell className="px-4">{application.email}</TableCell>
            <TableCell className="px-4">
              <StatusBadge status={application.status} />
            </TableCell>
            <TableCell className="px-4">
              <DateTime value={application.submitted_at} />
            </TableCell>
          </TableRow>
        ))}
      </DataTable>
      <Pagination page={applications} basePath={`/jobs/${jobPostingId}/applications`} />
    </>
  );
}
