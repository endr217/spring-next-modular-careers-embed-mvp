"use client";

import { useTranslations } from "next-intl";
import { useActionState } from "react";
import { FormMessage } from "@/shared/components/form-message";
import { SubmitButton } from "@/shared/components/submit-button";
import { TextField } from "@/shared/components/text-field";
import { FieldGroup } from "@/shared/components/ui/field";
import { EMPTY_FORM_STATE } from "@/shared/forms/form-state";
import { completeOnboarding } from "../actions/complete-onboarding";

const FIELDS = [
  { name: "company_name", type: "text", label: "name" },
  { name: "company_slug", type: "text", label: "slug" },
  { name: "company_email", type: "email", label: "email" },
] as const;

export function OnboardingForm() {
  const [state, formAction, pending] = useActionState(completeOnboarding, EMPTY_FORM_STATE);
  const t = useTranslations("onboarding.fields");

  return (
    <form action={formAction} noValidate>
      <FieldGroup>
        <FormMessage message={state.message} />
        {FIELDS.map((field) => (
          <TextField
            key={field.name}
            name={field.name}
            type={field.type}
            label={t(field.label)}
            defaultValue={state.values?.[field.name]}
            error={state.errors?.[field.name]}
          />
        ))}
        <SubmitButton pending={pending}>{t("submit")}</SubmitButton>
      </FieldGroup>
    </form>
  );
}
