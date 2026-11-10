"use client";

import { useTranslations } from "next-intl";
import { useActionState } from "react";
import { FormActions } from "@/shared/components/form-actions";
import { FormMessage } from "@/shared/components/form-message";
import { SelectField } from "@/shared/components/select-field";
import { TextField } from "@/shared/components/text-field";
import { FieldGroup } from "@/shared/components/ui/field";
import { EMPTY_FORM_STATE } from "@/shared/forms/form-state";
import { registerWebsite } from "../actions/register-website";
import { THEMES } from "../schema";

const FIELDS = [
  { name: "name", type: "text", placeholder: undefined },
  { name: "domain", type: "text", placeholder: "example.com" },
  { name: "branding_color", type: "text", placeholder: "#1d4ed8" },
  { name: "font_family", type: "text", placeholder: "Inter, sans-serif" },
] as const;

export function WebsiteForm() {
  const [state, formAction, pending] = useActionState(registerWebsite, EMPTY_FORM_STATE);
  const t = useTranslations("websites.fields");

  return (
    <form action={formAction} noValidate>
      <FieldGroup>
        <FormMessage message={state.message} />
        {FIELDS.map((field) => (
          <TextField
            key={field.name}
            name={field.name}
            type={field.type}
            placeholder={field.placeholder}
            label={t(field.name)}
            defaultValue={state.values?.[field.name]}
            error={state.errors?.[field.name]}
          />
        ))}
        <SelectField
          name="theme"
          label={t("theme")}
          placeholder={t("themePlaceholder")}
          options={THEMES.map((theme) => ({ value: theme, label: t(`themes.${theme}`) }))}
          defaultValue={state.values?.theme ?? "light"}
          error={state.errors?.theme}
        />
        <FormActions pending={pending} cancelHref="/websites" />
      </FieldGroup>
    </form>
  );
}
