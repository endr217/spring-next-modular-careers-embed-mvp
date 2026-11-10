"use server";

import { redirect } from "next/navigation";
import { ApiError } from "@/shared/api/api-error";
import { apiFetch } from "@/shared/api/api-fetch";
import type { FormState } from "@/shared/forms/form-state";
import { formValues } from "@/shared/forms/form-values";
import { parseForm } from "@/shared/forms/parse-form";
import { toFormState } from "@/shared/forms/to-form-state";
import { onboardingSchema } from "../schema";

export async function completeOnboarding(_: FormState, formData: FormData): Promise<FormState> {
  const values = formValues(formData);
  const parsed = await parseForm(onboardingSchema, values);
  if (!parsed.success) return { errors: parsed.errors, values };

  try {
    await apiFetch("/auth/onboarding/company", { method: "POST", body: parsed.data });
  } catch (error) {
    // A taken slug is reported for the slug field, together with a suggested free one.
    if (error instanceof ApiError && error.status === 422 && !Object.keys(error.errors).length) {
      return { errors: { company_slug: error.message }, values };
    }
    return toFormState(error, values);
  }
  redirect("/websites");
}
