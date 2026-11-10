export function toQuery(params: Record<string, string | number | undefined>) {
  const entries = Object.entries(params).filter(([, value]) => value !== undefined && value !== "");
  const query = new URLSearchParams(entries.map(([key, value]) => [key, String(value)]));
  return query.size ? `?${query}` : "";
}
