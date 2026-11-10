import Link from "next/link";
import { getTranslations } from "next-intl/server";
import { requireCompany } from "@/modules/companies/server";
import { DataTable } from "@/shared/components/data-table";
import { DateTime } from "@/shared/components/date-time";
import { EmptyRow } from "@/shared/components/empty-row";
import { TableCell, TableRow } from "@/shared/components/ui/table";
import { getWebsites } from "../queries/get-websites";

export async function WebsitesList() {
  const company = await requireCompany();
  const [t, websites] = await Promise.all([getTranslations("websites"), getWebsites(company.id)]);
  const head = [t("fields.name"), t("fields.domain"), t("fields.theme"), t("createdAt")];

  return (
    <DataTable head={head}>
      {websites.length === 0 && <EmptyRow colSpan={head.length} text={t("empty")} />}
      {websites.map((website) => (
        <TableRow key={website.id}>
          <TableCell className="px-4 font-medium">
            <Link href={`/websites/${website.id}`} className="underline-offset-4 hover:underline">
              {website.name}
            </Link>
          </TableCell>
          <TableCell className="px-4">{website.domain}</TableCell>
          <TableCell className="px-4">{t(`fields.themes.${website.theme}`)}</TableCell>
          <TableCell className="px-4">
            <DateTime value={website.created_at} />
          </TableCell>
        </TableRow>
      ))}
    </DataTable>
  );
}
