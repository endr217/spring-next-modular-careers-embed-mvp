"use client";

import { useTranslations } from "next-intl";
import { useActionState } from "react";
import { FormMessage } from "@/shared/components/form-message";
import { SubmitButton } from "@/shared/components/submit-button";
import { TextField } from "@/shared/components/text-field";
import { FieldGroup } from "@/shared/components/ui/field";
import { EMPTY_FORM_STATE } from "@/shared/forms/form-state";
import { resetPassword } from "../actions/reset-password";

export function ResetPasswordForm({ token, email }: { token: string; email: string }) {
  const [state, formAction, pending] = useActionState(resetPassword, EMPTY_FORM_STATE);
  const t = useTranslations("auth");

  return (
    <form action={formAction} noValidate>
      <FieldGroup>
        <FormMessage message={state.message ?? state.errors?.token} />
        <input type="hidden" name="token" value={state.values?.token ?? token} />
        <TextField
          name="email"
          type="email"
          label={t("email")}
          autoComplete="email"
          defaultValue={state.values?.email ?? email}
          error={state.errors?.email}
        />
        <TextField
          name="password"
          type="password"
          label={t("password")}
          autoComplete="new-password"
          error={state.errors?.password}
        />
        <TextField
          name="password_confirmation"
          type="password"
          label={t("passwordConfirmation")}
          autoComplete="new-password"
          error={state.errors?.password_confirmation}
        />
        <SubmitButton pending={pending}>{t("reset.submit")}</SubmitButton>
      </FieldGroup>
    </form>
  );
}
