import "server-only";
import { apiFetch } from "@/shared/api/api-fetch";
import { orNotFound } from "@/shared/api/or-not-found";
import { toQuery } from "@/shared/api/to-query";
import type { Page } from "@/shared/types";
import type { JobPosting } from "../types";

export function getJobPostings(websiteId: number | string, page = 1) {
  return apiFetch<Page<JobPosting>>(`/websites/${websiteId}/job-postings${toQuery({ page })}`);
}

export function getJobPosting(id: number | string) {
  return orNotFound(apiFetch<JobPosting>(`/job-postings/${id}`));
}
