import { getTranslations } from "next-intl/server";
import { Suspense } from "react";
import { AuthCard } from "@/app/(auth)/_components/auth-card";
import { ResetPasswordForm } from "@/modules/auth/components/reset-password-form";
import { LoadingText } from "@/shared/components/loading-text";
import { readParam } from "@/shared/lib/search-params";

async function ResetPassword({ searchParams }: Pick<PageProps<"/reset-password">, "searchParams">) {
  const params = await searchParams;
  return <ResetPasswordForm token={readParam(params, "token")} email={readParam(params, "email")} />;
}

export default async function ResetPasswordPage({ searchParams }: PageProps<"/reset-password">) {
  const t = await getTranslations("auth.reset");

  return (
    <AuthCard title={t("title")} description={t("description")}>
      <Suspense fallback={<LoadingText />}>
        <ResetPassword searchParams={searchParams} />
      </Suspense>
    </AuthCard>
  );
}
