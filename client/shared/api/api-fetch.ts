import "server-only";
import { redirect } from "next/navigation";
import { getLocale } from "next-intl/server";
import { getToken } from "@/shared/auth/session";
import { apiUrl } from "@/shared/api/api-url";
import { toApiError } from "@/shared/api/to-api-error";

type Options = Omit<RequestInit, "body"> & { body?: unknown; auth?: boolean };

export async function apiFetch<T = void>(path: string, options: Options = {}): Promise<T> {
  const { body, auth = true, ...init } = options;
  const token = auth ? await getToken() : undefined;
  if (auth && !token) redirect("/login");

  const response = await fetch(`${apiUrl()}${path}`, {
    ...init,
    cache: "no-store",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
      "Accept-Language": await getLocale(),
      ...(token && { Authorization: `Bearer ${token}` }),
    },
    body: body === undefined ? undefined : JSON.stringify(body),
  });

  if (auth && response.status === 401) redirect("/logout");
  if (!response.ok) throw await toApiError(response);
  return (response.status === 204 ? undefined : await response.json()) as T;
}
