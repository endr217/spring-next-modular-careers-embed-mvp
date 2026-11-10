"use client";

import Link from "next/link";
import { useTranslations } from "next-intl";
import { useActionState } from "react";
import { FormMessage } from "@/shared/components/form-message";
import { SubmitButton } from "@/shared/components/submit-button";
import { SuccessMessage } from "@/shared/components/success-message";
import { TextField } from "@/shared/components/text-field";
import { FieldGroup } from "@/shared/components/ui/field";
import { EMPTY_FORM_STATE } from "@/shared/forms/form-state";
import { forgotPassword } from "../actions/forgot-password";

export function ForgotPasswordForm() {
  const [state, formAction, pending] = useActionState(forgotPassword, EMPTY_FORM_STATE);
  const t = useTranslations("auth");

  return (
    <form action={formAction} noValidate>
      <FieldGroup>
        <FormMessage message={state.message} />
        <SuccessMessage message={state.success} />
        <TextField
          name="email"
          type="email"
          label={t("email")}
          autoComplete="email"
          defaultValue={state.values?.email}
          error={state.errors?.email}
        />
        <SubmitButton pending={pending}>{t("forgot.submit")}</SubmitButton>
        <Link href="/login" className="text-sm underline">
          {t("forgot.back")}
        </Link>
      </FieldGroup>
    </form>
  );
}
