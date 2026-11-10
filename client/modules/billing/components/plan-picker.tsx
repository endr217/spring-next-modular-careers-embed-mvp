"use client";

import { useTranslations } from "next-intl";
import { useActionState } from "react";
import { FormMessage } from "@/shared/components/form-message";
import { SubmitButton } from "@/shared/components/submit-button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/shared/components/ui/card";
import { EMPTY_FORM_STATE } from "@/shared/forms/form-state";
import { startCheckout } from "../actions/start-checkout";
import type { Plan } from "../types";

const PLANS: Plan[] = ["monthly", "yearly"];

export function PlanPicker() {
  const [state, formAction, pending] = useActionState(startCheckout, EMPTY_FORM_STATE);
  const t = useTranslations("billing");

  return (
    <div className="grid gap-4">
      <FormMessage message={state.message} />
      <div className="grid gap-4 sm:grid-cols-2">
        {PLANS.map((plan) => (
          <Card key={plan}>
            <CardHeader>
              <CardTitle>{t(`plans.${plan}.title`)}</CardTitle>
              <CardDescription>{t(`plans.${plan}.description`)}</CardDescription>
            </CardHeader>
            <CardContent>
              <form action={formAction}>
                <input type="hidden" name="plan" value={plan} />
                <SubmitButton pending={pending}>{t("subscribe")}</SubmitButton>
              </form>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}
