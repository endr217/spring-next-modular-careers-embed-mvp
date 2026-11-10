import "server-only";
import { getTranslations } from "next-intl/server";
import type { z } from "zod";
import { validationKey } from "@/i18n/keys";

type Parsed<T> =
  | { success: true; data: T }
  | { success: false; errors: Record<string, string> };

export async function parseForm<S extends z.ZodType>(
  schema: S,
  input: unknown,
): Promise<Parsed<z.output<S>>> {
  const result = schema.safeParse(input);
  if (result.success) return { success: true, data: result.data };

  const t = await getTranslations("validation");
  const errors: Record<string, string> = {};
  for (const issue of result.error.issues) {
    errors[issue.path.join(".")] ??= t(validationKey(issue.message));
  }
  return { success: false, errors };
}
