import { getTranslations } from "next-intl/server";
import { AuthCard } from "@/app/(auth)/_components/auth-card";
import { RegisterForm } from "@/modules/auth/components/register-form";

export default async function RegisterPage() {
  const t = await getTranslations("auth.register");

  return (
    <AuthCard title={t("title")} description={t("description")}>
      <RegisterForm />
    </AuthCard>
  );
}
