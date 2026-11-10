import { getTranslations } from "next-intl/server";
import { AuthCard } from "@/app/(auth)/_components/auth-card";
import { LoginForm } from "@/modules/auth/components/login-form";

export default async function LoginPage() {
  const t = await getTranslations("auth.login");

  return (
    <AuthCard title={t("title")} description={t("description")}>
      <LoginForm />
    </AuthCard>
  );
}
