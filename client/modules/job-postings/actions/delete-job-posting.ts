"use server";

import { refresh } from "next/cache";
import { apiFetch } from "@/shared/api/api-fetch";
import type { FormState } from "@/shared/forms/form-state";
import { toFormState } from "@/shared/forms/to-form-state";

export async function deleteJobPosting(id: number): Promise<FormState> {
  try {
    await apiFetch(`/job-postings/${id}`, { method: "DELETE" });
  } catch (error) {
    return toFormState(error);
  }
  refresh();
  return {};
}
