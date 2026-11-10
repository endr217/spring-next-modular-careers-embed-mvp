import { getTranslations } from "next-intl/server";
import { AuthCard } from "@/app/(auth)/_components/auth-card";
import { ForgotPasswordForm } from "@/modules/auth/components/forgot-password-form";

export default async function ForgotPasswordPage() {
  const t = await getTranslations("auth.forgot");

  return (
    <AuthCard title={t("title")} description={t("description")}>
      <ForgotPasswordForm />
    </AuthCard>
  );
}
