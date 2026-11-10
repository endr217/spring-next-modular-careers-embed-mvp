import { NextResponse, type NextRequest } from "next/server";
import { TOKEN_COOKIE } from "@/shared/auth/token-cookie";

export function proxy(request: NextRequest) {
  if (request.cookies.has(TOKEN_COOKIE)) return NextResponse.next();
  return NextResponse.redirect(new URL("/login", request.url));
}

export const config = {
  matcher: ["/", "/onboarding", "/websites/:path*", "/jobs/:path*", "/applications/:path*", "/company/:path*"],
};
