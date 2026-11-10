import "server-only";
import { apiFetch } from "@/shared/api/api-fetch";
import { toQuery } from "@/shared/api/to-query";
import type { LoadStrategy } from "../domain/load-strategy";
import type { EmbedCode } from "../types";

export function getEmbedCode(websiteSlug: string, loadStrategy: LoadStrategy) {
  return apiFetch<EmbedCode>(
    `/websites/${encodeURIComponent(websiteSlug)}/embed-code${toQuery({ loadStrategy })}`,
    { auth: false },
  );
}
