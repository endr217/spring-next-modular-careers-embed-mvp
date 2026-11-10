import { getTranslations } from "next-intl/server";
import { PlanPicker } from "@/modules/billing/components/plan-picker";
import { PageHeader } from "@/shared/components/page-header";

export default async function SubscriptionPage() {
  const t = await getTranslations("billing");

  return (
    <>
      <PageHeader title={t("title")} />
      <PlanPicker />
    </>
  );
}
