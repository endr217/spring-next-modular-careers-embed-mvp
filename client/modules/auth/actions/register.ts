"use server";

import type { FormState } from "@/shared/forms/form-state";
import { registerSchema } from "../schema";
import { authenticate } from "./authenticate";

export async function register(_: FormState, formData: FormData) {
  return authenticate("/auth/register", registerSchema, formData);
}
