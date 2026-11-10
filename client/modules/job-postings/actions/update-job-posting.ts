"use server";

import type { FormState } from "@/shared/forms/form-state";
import { saveJobPosting } from "./save-job-posting";

export async function updateJobPosting(id: number, websiteId: number, _: FormState, formData: FormData) {
  return saveJobPosting({ path: `/job-postings/${id}`, method: "PUT", websiteId }, formData);
}
