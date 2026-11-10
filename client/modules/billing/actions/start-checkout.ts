"use server";

import { redirect } from "next/navigation";
import { apiFetch } from "@/shared/api/api-fetch";
import type { FormState } from "@/shared/forms/form-state";
import { toFormState } from "@/shared/forms/to-form-state";
import { priceId, toPlan } from "../plans";
import type { CheckoutSession } from "../types";

export async function startCheckout(_: FormState, formData: FormData): Promise<FormState> {
  const plan = toPlan(formData.get("plan")?.toString());
  if (!plan) return {};

  let session: CheckoutSession;
  try {
    session = await apiFetch<CheckoutSession>("/checkout", {
      method: "POST",
      body: { price_id: priceId(plan), quantity: 1, plan_type: "default", mode: "subscription" },
    });
  } catch (error) {
    return toFormState(error);
  }
  redirect(session.url);
}
