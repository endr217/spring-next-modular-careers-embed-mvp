import "server-only";
import { getTranslations } from "next-intl/server";
import { ApiError } from "@/shared/api/api-error";
import type { FormState } from "@/shared/forms/form-state";

export async function toFormState(error: unknown, values?: Record<string, string>): Promise<FormState> {
  if (!(error instanceof ApiError)) throw error;
  const t = await getTranslations("common");
  return {
    message: error.message || t("serverError", { status: error.status }),
    errors: error.errors,
    values,
  };
}
