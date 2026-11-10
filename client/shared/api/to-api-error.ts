import { ApiError } from "@/shared/api/api-error";

type ErrorBody = {
  message?: string;
  errors?: Record<string, string[] | string>;
  error?: { message?: string } | boolean;
};

const firstMessage = (value: string[] | string) => (Array.isArray(value) ? value[0] : value);

export async function toApiError(response: Response) {
  const body: ErrorBody = await response.json().catch(() => ({}));
  const errors = Object.fromEntries(
    Object.entries(body.errors ?? {}).map(([field, messages]) => [field, firstMessage(messages)]),
  );
  const nested = typeof body.error === "object" ? body.error.message : undefined;
  return new ApiError(response.status, body.message ?? nested ?? "", errors);
}
