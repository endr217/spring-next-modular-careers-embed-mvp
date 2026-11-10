import "server-only";
import { cookies } from "next/headers";
import { TOKEN_COOKIE } from "@/shared/auth/token-cookie";

export async function getToken() {
  return (await cookies()).get(TOKEN_COOKIE)?.value;
}

/** Seconds until the JWT expires, read from its own exp claim. */
function secondsLeft(token: string) {
  try {
    const payload = JSON.parse(Buffer.from(token.split(".")[1], "base64url").toString());
    return Math.max(0, Math.floor(payload.exp - Date.now() / 1000));
  } catch {
    return undefined;
  }
}

export async function saveToken(token: string) {
  (await cookies()).set(TOKEN_COOKIE, token, {
    httpOnly: true,
    sameSite: "lax",
    secure: process.env.NODE_ENV === "production",
    path: "/",
    maxAge: secondsLeft(token),
  });
}

export async function clearToken() {
  (await cookies()).delete(TOKEN_COOKIE);
}
