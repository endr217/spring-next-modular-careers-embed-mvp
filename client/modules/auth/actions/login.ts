"use server";

import type { FormState } from "@/shared/forms/form-state";
import { loginSchema } from "../schema";
import { authenticate } from "./authenticate";

export async function login(_: FormState, formData: FormData) {
  return authenticate("/auth/login", loginSchema, formData);
}
