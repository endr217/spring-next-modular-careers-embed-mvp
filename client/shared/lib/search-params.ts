type SearchParams = Record<string, string | string[] | undefined>;

export function readParam(params: SearchParams, key: string) {
  const value = params[key];
  return (Array.isArray(value) ? value[0] : value) ?? "";
}

/** One-based page number, as the API expects it. */
export function readPage(params: SearchParams) {
  const page = Number(readParam(params, "page"));
  return Number.isInteger(page) && page > 0 ? page : 1;
}
