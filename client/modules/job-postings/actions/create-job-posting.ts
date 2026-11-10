"use server";

import type { FormState } from "@/shared/forms/form-state";
import { saveJobPosting } from "./save-job-posting";

export async function createJobPosting(websiteId: number, _: FormState, formData: FormData) {
  return saveJobPosting({ path: "/job-postings", method: "POST", websiteId }, formData);
}
