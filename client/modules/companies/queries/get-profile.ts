import "server-only";
import { redirect } from "next/navigation";
import { apiFetch } from "@/shared/api/api-fetch";
import type { Profile, ProfileCompany } from "../types";

export function getProfile() {
  return apiFetch<Profile>("/auth/user");
}

/** The signed-in user's primary company; users without one finish onboarding first. */
export async function requireCompany(): Promise<ProfileCompany> {
  const profile = await getProfile();
  if (!profile.primary_company) redirect("/onboarding");
  return profile.primary_company;
}
