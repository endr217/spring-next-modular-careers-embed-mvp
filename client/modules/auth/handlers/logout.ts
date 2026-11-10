import { NextResponse, type NextRequest } from "next/server";
import { apiFetch } from "@/shared/api/api-fetch";
import { clearToken, getToken } from "@/shared/auth/session";

export async function POST(request: NextRequest) {
  if (await getToken()) await apiFetch("/auth/logout", { method: "POST" }).catch(() => undefined);
  await clearToken();
  return NextResponse.redirect(new URL("/login", request.url), 303);
}
