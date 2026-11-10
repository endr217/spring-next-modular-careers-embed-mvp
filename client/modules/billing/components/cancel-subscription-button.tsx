"use client";

import { useTranslations } from "next-intl";
import { useActionState } from "react";
import { FormMessage } from "@/shared/components/form-message";
import { SubmitButton } from "@/shared/components/submit-button";
import { SuccessMessage } from "@/shared/components/success-message";
import { EMPTY_FORM_STATE } from "@/shared/forms/form-state";
import { cancelSubscription } from "../actions/cancel-subscription";

export function CancelSubscriptionButton({ subscriptionId }: { subscriptionId: string }) {
  const [state, formAction, pending] = useActionState(
    cancelSubscription.bind(null, subscriptionId),
    EMPTY_FORM_STATE,
  );
  const t = useTranslations("billing");

  if (state.success) return <SuccessMessage message={state.success} />;

  const confirm = (event: React.FormEvent) => {
    if (!window.confirm(t("cancelConfirm"))) event.preventDefault();
  };

  return (
    <form action={formAction} onSubmit={confirm} className="grid gap-2">
      <FormMessage message={state.message} />
      <SubmitButton pending={pending} variant="outline">
        {t("cancelSubscription")}
      </SubmitButton>
    </form>
  );
}
