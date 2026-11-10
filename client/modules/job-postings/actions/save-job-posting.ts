import "server-only";
import { redirect } from "next/navigation";
import { apiFetch } from "@/shared/api/api-fetch";
import type { FormState } from "@/shared/forms/form-state";
import { formValues } from "@/shared/forms/form-values";
import { parseForm } from "@/shared/forms/parse-form";
import { toFormState } from "@/shared/forms/to-form-state";
import { jobPostingSchema } from "../schema";

type Target = { path: string; method: "POST" | "PUT"; websiteId: number };

export async function saveJobPosting({ path, method, websiteId }: Target, formData: FormData): Promise<FormState> {
  const values = formValues(formData);
  const parsed = await parseForm(jobPostingSchema, values);
  if (!parsed.success) return { errors: parsed.errors, values };

  try {
    await apiFetch(path, { method, body: { ...parsed.data, website_id: websiteId } });
  } catch (error) {
    return toFormState(error, values);
  }
  redirect(`/websites/${websiteId}`);
}
