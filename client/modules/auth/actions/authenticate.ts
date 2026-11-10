import "server-only";
import { redirect } from "next/navigation";
import type { z } from "zod";
import { apiFetch } from "@/shared/api/api-fetch";
import { saveToken } from "@/shared/auth/session";
import type { FormState } from "@/shared/forms/form-state";
import { formValues } from "@/shared/forms/form-values";
import { parseForm } from "@/shared/forms/parse-form";
import { toFormState } from "@/shared/forms/to-form-state";
import type { AuthResponse } from "../types";

/** Posts the credentials, stores the issued token and opens the dashboard. */
export async function authenticate(path: string, schema: z.ZodType, formData: FormData): Promise<FormState> {
  const input = formValues(formData);
  const values = Object.fromEntries(Object.entries(input).filter(([key]) => key !== "password"));
  const parsed = await parseForm(schema, input);
  if (!parsed.success) return { errors: parsed.errors, values };

  try {
    const { token } = await apiFetch<AuthResponse>(path, { method: "POST", body: parsed.data, auth: false });
    await saveToken(token);
  } catch (error) {
    return toFormState(error, values);
  }
  redirect("/");
}
