"use server";

import { redirect } from "next/navigation";
import { apiFetch } from "@/shared/api/api-fetch";
import type { FormState } from "@/shared/forms/form-state";
import { formValues } from "@/shared/forms/form-values";
import { parseForm } from "@/shared/forms/parse-form";
import { toFormState } from "@/shared/forms/to-form-state";
import { resetPasswordSchema } from "../schema";
import type { PasswordResetResponse } from "../types";

export async function resetPassword(_: FormState, formData: FormData): Promise<FormState> {
  const input = formValues(formData);
  const values = { token: input.token ?? "", email: input.email ?? "" };
  const parsed = await parseForm(resetPasswordSchema, input);
  if (!parsed.success) return { errors: parsed.errors, values };

  try {
    const result = await apiFetch<PasswordResetResponse>("/auth/reset-password", {
      method: "POST",
      body: parsed.data,
      auth: false,
    });
    if (!result.success) return { message: result.message, values };
  } catch (error) {
    return toFormState(error, values);
  }
  redirect("/login?reset=1");
}
