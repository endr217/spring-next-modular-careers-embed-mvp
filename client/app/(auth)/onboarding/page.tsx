import { getTranslations } from "next-intl/server";
import { AuthCard } from "@/app/(auth)/_components/auth-card";
import { OnboardingForm } from "@/modules/companies/components/onboarding-form";

export default async function OnboardingPage() {
  const t = await getTranslations("onboarding");

  return (
    <AuthCard title={t("title")} description={t("description")}>
      <OnboardingForm />
    </AuthCard>
  );
}
