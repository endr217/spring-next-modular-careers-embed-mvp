"use client";

import { useTranslations } from "next-intl";
import { useActionState } from "react";
import { FormActions } from "@/shared/components/form-actions";
import { FormMessage } from "@/shared/components/form-message";
import { SelectField } from "@/shared/components/select-field";
import { TextField } from "@/shared/components/text-field";
import { TextareaField } from "@/shared/components/textarea-field";
import { Field, FieldGroup, FieldLabel } from "@/shared/components/ui/field";
import { EMPTY_FORM_STATE, type FormState } from "@/shared/forms/form-state";
import { JOB_TYPES } from "../schema";
import type { JobPosting } from "../types";

type Props = {
  action: (prev: FormState, formData: FormData) => Promise<FormState>;
  websiteId: number;
  posting?: JobPosting;
};

const initialValues = (posting?: JobPosting): Record<string, string> => ({
  title: posting?.title ?? "",
  location: posting?.location ?? "",
  job_type: posting?.job_type ?? "",
  salary_from: posting?.salary_from?.toString() ?? "",
  salary_to: posting?.salary_to?.toString() ?? "",
  description: posting?.description ?? "",
  expires_at: posting?.expires_at?.slice(0, 10) ?? "",
});

export function JobPostingForm({ action, websiteId, posting }: Props) {
  const [state, formAction, pending] = useActionState(action, EMPTY_FORM_STATE);
  const t = useTranslations("jobPostings.fields");
  const values = state.values ?? initialValues(posting);
  const active = state.values ? state.values.is_active === "on" : (posting?.is_active ?? true);

  return (
    <form action={formAction} noValidate>
      <FieldGroup>
        <FormMessage message={state.message} />
        <TextField name="title" label={t("title")} defaultValue={values.title} error={state.errors?.title} />
        <TextField name="location" label={t("location")} defaultValue={values.location} error={state.errors?.location} />
        <SelectField
          name="job_type"
          label={t("job_type")}
          placeholder={t("jobTypePlaceholder")}
          options={JOB_TYPES.map((type) => ({ value: type, label: t(`jobTypes.${type}`) }))}
          defaultValue={values.job_type}
          error={state.errors?.job_type}
        />
        <div className="grid gap-4 sm:grid-cols-2">
          <TextField
            name="salary_from"
            inputMode="numeric"
            label={t("salary_from")}
            defaultValue={values.salary_from}
            error={state.errors?.salary_from}
          />
          <TextField
            name="salary_to"
            inputMode="numeric"
            label={t("salary_to")}
            defaultValue={values.salary_to}
            error={state.errors?.salary_to}
          />
        </div>
        <TextareaField
          name="description"
          rows={8}
          label={t("description")}
          defaultValue={values.description}
          error={state.errors?.description}
        />
        <TextField
          name="expires_at"
          type="date"
          label={t("expires_at")}
          defaultValue={values.expires_at}
          error={state.errors?.expires_at}
        />
        <Field orientation="horizontal">
          <input id="is_active" name="is_active" type="checkbox" defaultChecked={active} className="size-4" />
          <FieldLabel htmlFor="is_active">{t("is_active")}</FieldLabel>
        </Field>
        <FormActions pending={pending} cancelHref={`/websites/${websiteId}`} />
      </FieldGroup>
    </form>
  );
}
