"use server";

import { redirect } from "next/navigation";
import { apiFetch } from "@/shared/api/api-fetch";
import type { FormState } from "@/shared/forms/form-state";
import { toFormState } from "@/shared/forms/to-form-state";

export async function deleteApplication(id: number, jobPostingId: number): Promise<FormState> {
  try {
    await apiFetch(`/applications/${id}`, { method: "DELETE" });
  } catch (error) {
    return toFormState(error);
  }
  redirect(`/jobs/${jobPostingId}/applications`);
}
