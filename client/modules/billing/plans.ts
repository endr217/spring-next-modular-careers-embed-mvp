import "server-only";
import type { Plan } from "./types";

export const PLANS: readonly Plan[] = ["monthly", "yearly"];

const PRICE_ENV: Record<Plan, string> = {
  monthly: "STRIPE_PRICE_MONTHLY",
  yearly: "STRIPE_PRICE_YEARLY",
};

/** Stripe price id of a plan; price ids are configuration, not secrets, but stay server-side. */
export function priceId(plan: Plan) {
  const value = process.env[PRICE_ENV[plan]];
  if (!value) throw new Error(`${PRICE_ENV[plan]} environment variable is required`);
  return value;
}

export function toPlan(value: string | undefined): Plan | undefined {
  return PLANS.find((plan) => plan === value);
}
