export function formValues(formData: FormData): Record<string, string> {
  const entries = [...formData.entries()].filter(
    ([key, value]) => !key.startsWith("$") && typeof value === "string",
  );
  return Object.fromEntries(entries) as Record<string, string>;
}
