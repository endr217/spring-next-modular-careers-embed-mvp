import "server-only";

export function apiUrl() {
  const url = process.env.API_URL;
  if (!url) throw new Error("API_URL environment variable is required");
  return url;
}
