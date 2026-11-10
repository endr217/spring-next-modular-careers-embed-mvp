import "server-only";
import { apiFetch } from "@/shared/api/api-fetch";
import type { Website, WebsiteDetails } from "../types";

export function getWebsites(companyId: number) {
  return apiFetch<Website[]>(`/websites/company/${companyId}`);
}

export function getWebsite(id: number | string) {
  return apiFetch<WebsiteDetails>(`/websites/${id}`);
}
