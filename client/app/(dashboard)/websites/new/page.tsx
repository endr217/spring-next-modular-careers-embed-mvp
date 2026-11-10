import { getTranslations } from "next-intl/server";
import { WebsiteForm } from "@/modules/websites/components/website-form";
import { FormCard } from "@/shared/components/form-card";

export default async function NewWebsitePage() {
  const t = await getTranslations("websites");

  return (
    <FormCard title={t("new")}>
      <WebsiteForm />
    </FormCard>
  );
}
