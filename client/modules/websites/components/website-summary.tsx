import { getTranslations } from "next-intl/server";
import { Card, CardContent } from "@/shared/components/ui/card";
import type { WebsiteDetails } from "../types";

export async function WebsiteSummary({ website }: { website: WebsiteDetails }) {
  const t = await getTranslations("websites");
  const rows = [
    [t("fields.domain"), website.domain],
    [t("fields.company"), website.company.name],
    [t("fields.slug"), website.slug],
    [t("fields.theme"), t(`fields.themes.${website.theme}`)],
    [t("fields.branding_color"), website.branding_color ?? t("none")],
    [t("fields.font_family"), website.font_family ?? t("none")],
    [t("jobPostingsCount"), String(website.job_postings_count)],
  ];

  return (
    <Card>
      <CardContent>
        <dl className="grid gap-x-6 gap-y-2 text-sm sm:grid-cols-[max-content_1fr]">
          {rows.map(([label, value]) => (
            <div key={label} className="contents">
              <dt className="text-muted-foreground">{label}</dt>
              <dd className="font-medium break-all">{value}</dd>
            </div>
          ))}
        </dl>
      </CardContent>
    </Card>
  );
}
