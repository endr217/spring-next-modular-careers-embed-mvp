import "server-only";
import { apiFetch } from "@/shared/api/api-fetch";
import { orNotFound } from "@/shared/api/or-not-found";
import { toQuery } from "@/shared/api/to-query";
import type { Page } from "@/shared/types";
import type { Application } from "../types";

export function getApplications(jobPostingId: number | string, page = 1) {
  return orNotFound(apiFetch<Page<Application>>(`/applications/job/${jobPostingId}${toQuery({ page })}`));
}

export function getApplication(id: number | string) {
  return orNotFound(apiFetch<Application>(`/applications/${id}`));
}
