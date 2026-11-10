import { getTranslations } from "next-intl/server";
import { FormCard } from "@/shared/components/form-card";
import { LinkButton } from "@/shared/components/link-button";

export default async function CheckoutCancelPage() {
  const t = await getTranslations("billing.checkout");

  return (
    <FormCard title={t("cancelTitle")}>
      <div className="grid gap-4">
        <p className="text-sm text-muted-foreground">{t("cancelDescription")}</p>
        <LinkButton href="/company/subscription" variant="outline">
          {t("backToPlans")}
        </LinkButton>
      </div>
    </FormCard>
  );
}
