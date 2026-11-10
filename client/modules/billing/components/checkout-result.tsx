import { notFound } from "next/navigation";
import { getTranslations } from "next-intl/server";
import { LinkButton } from "@/shared/components/link-button";
import { SuccessMessage } from "@/shared/components/success-message";
import { FormMessage } from "@/shared/components/form-message";
import { verifyPayment } from "../queries/verify-payment";
import { CancelSubscriptionButton } from "./cancel-subscription-button";

/** Confirms the Stripe checkout session and stores the resulting subscription. */
export async function CheckoutResult({ sessionId }: { sessionId: string }) {
  if (!sessionId) notFound();
  const [t, payment] = await Promise.all([getTranslations("billing"), verifyPayment(sessionId)]);
  const paid = payment.status === "paid" || payment.status === "no_payment_required";

  return (
    <div className="grid gap-4">
      {paid ? (
        <SuccessMessage message={t("checkout.paid")} />
      ) : (
        <FormMessage message={t("checkout.unpaid", { status: payment.status })} />
      )}
      {payment.subscriptionId && (
        <p className="text-sm text-muted-foreground">
          {t("checkout.subscription", { id: payment.subscriptionId })}
        </p>
      )}
      <div className="flex flex-wrap gap-2">
        <LinkButton href="/websites">{t("checkout.continue")}</LinkButton>
        {payment.subscriptionId && <CancelSubscriptionButton subscriptionId={payment.subscriptionId} />}
      </div>
    </div>
  );
}
