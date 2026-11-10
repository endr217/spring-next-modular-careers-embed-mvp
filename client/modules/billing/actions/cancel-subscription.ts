"use server";

import { apiFetch } from "@/shared/api/api-fetch";
import type { FormState } from "@/shared/forms/form-state";
import { toFormState } from "@/shared/forms/to-form-state";

export async function cancelSubscription(subscriptionId: string): Promise<FormState> {
  try {
    const result = await apiFetch<{ message: string }>("/cancel-subscription", {
      method: "POST",
      body: { subscription_id: subscriptionId },
    });
    return { success: result.message };
  } catch (error) {
    return toFormState(error);
  }
}
