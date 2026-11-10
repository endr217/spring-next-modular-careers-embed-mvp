"use client";

import Link from "next/link";
import { useTranslations } from "next-intl";
import { useActionState } from "react";
import { FormMessage } from "@/shared/components/form-message";
import { SubmitButton } from "@/shared/components/submit-button";
import { TextField } from "@/shared/components/text-field";
import { FieldGroup } from "@/shared/components/ui/field";
import { EMPTY_FORM_STATE } from "@/shared/forms/form-state";
import { register } from "../actions/register";

const FIELDS = [
  { name: "name", type: "text", autoComplete: "given-name" },
  { name: "surname", type: "text", autoComplete: "family-name" },
  { name: "email", type: "email", autoComplete: "email" },
] as const;

export function RegisterForm() {
  const [state, formAction, pending] = useActionState(register, EMPTY_FORM_STATE);
  const t = useTranslations("auth");

  return (
    <form action={formAction} noValidate>
      <FieldGroup>
        <FormMessage message={state.message} />
        {FIELDS.map((field) => (
          <TextField
            key={field.name}
            {...field}
            label={t(field.name)}
            defaultValue={state.values?.[field.name]}
            error={state.errors?.[field.name]}
          />
        ))}
        <TextField
          name="password"
          type="password"
          label={t("password")}
          autoComplete="new-password"
          error={state.errors?.password}
        />
        <SubmitButton pending={pending}>{t("register.submit")}</SubmitButton>
        <Link href="/login" className="text-sm underline">
          {t("register.login")}
        </Link>
      </FieldGroup>
    </form>
  );
}
