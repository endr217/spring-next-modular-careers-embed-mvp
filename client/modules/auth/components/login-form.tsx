"use client";

import Link from "next/link";
import { useTranslations } from "next-intl";
import { useActionState } from "react";
import { FormMessage } from "@/shared/components/form-message";
import { SubmitButton } from "@/shared/components/submit-button";
import { TextField } from "@/shared/components/text-field";
import { FieldGroup } from "@/shared/components/ui/field";
import { EMPTY_FORM_STATE } from "@/shared/forms/form-state";
import { login } from "../actions/login";

export function LoginForm() {
  const [state, formAction, pending] = useActionState(login, EMPTY_FORM_STATE);
  const t = useTranslations("auth");

  return (
    <form action={formAction} noValidate>
      <FieldGroup>
        <FormMessage message={state.message} />
        <TextField
          name="email"
          type="email"
          label={t("email")}
          autoComplete="email"
          defaultValue={state.values?.email}
          error={state.errors?.email}
        />
        <TextField
          name="password"
          type="password"
          label={t("password")}
          autoComplete="current-password"
          error={state.errors?.password}
        />
        <SubmitButton pending={pending}>{t("login.submit")}</SubmitButton>
        <div className="flex justify-between text-sm">
          <Link href="/forgot-password" className="underline">
            {t("login.forgot")}
          </Link>
          <Link href="/register" className="underline">
            {t("login.register")}
          </Link>
        </div>
      </FieldGroup>
    </form>
  );
}
