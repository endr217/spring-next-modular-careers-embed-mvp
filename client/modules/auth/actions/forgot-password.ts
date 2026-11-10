"use server";

import { apiFetch } from "@/shared/api/api-fetch";
import type { FormState } from "@/shared/forms/form-state";
import { formValues } from "@/shared/forms/form-values";
import { parseForm } from "@/shared/forms/parse-form";
import { toFormState } from "@/shared/forms/to-form-state";
import { forgotPasswordSchema } from "../schema";
import type { PasswordResetResponse } from "../types";

export async function forgotPassword(_: FormState, formData: FormData): Promise<FormState> {
  const values = formValues(formData);
  const parsed = await parseForm(forgotPasswordSchema, values);
  if (!parsed.success) return { errors: parsed.errors, values };

  try {
    const result = await apiFetch<PasswordResetResponse>("/auth/forgot-password", {
      method: "POST",
      body: parsed.data,
      auth: false,
    });
    return result.success ? { success: result.message } : { message: result.message, values };
  } catch (error) {
    return toFormState(error, values);
  }
}
