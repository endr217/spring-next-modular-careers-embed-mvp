import "server-only";
import { apiFetch } from "@/shared/api/api-fetch";
import { orNotFound } from "@/shared/api/or-not-found";
import { toQuery } from "@/shared/api/to-query";
import type { PaymentVerification } from "../types";

export function verifyPayment(sessionId: string) {
  return orNotFound(apiFetch<PaymentVerification>(`/verify-payment${toQuery({ session_id: sessionId })}`));
}
