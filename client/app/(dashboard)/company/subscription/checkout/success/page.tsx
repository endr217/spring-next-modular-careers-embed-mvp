import { getTranslations } from "next-intl/server";
import { Suspense } from "react";
import { CheckoutResult } from "@/modules/billing/components/checkout-result";
import { FormCard } from "@/shared/components/form-card";
import { LoadingText } from "@/shared/components/loading-text";
import { readParam } from "@/shared/lib/search-params";

type Props = PageProps<"/company/subscription/checkout/success">;

async function Result({ searchParams }: Pick<Props, "searchParams">) {
  const params = await searchParams;
  return <CheckoutResult sessionId={readParam(params, "session_id")} />;
}

export default async function CheckoutSuccessPage({ searchParams }: Props) {
  const t = await getTranslations("billing.checkout");

  return (
    <FormCard title={t("successTitle")}>
      <Suspense fallback={<LoadingText />}>
        <Result searchParams={searchParams} />
      </Suspense>
    </FormCard>
  );
}
