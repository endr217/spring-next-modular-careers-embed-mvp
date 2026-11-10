export type Plan = "monthly" | "yearly";

export type CheckoutSession = { sessionId: string; url: string };

export type PaymentVerification = {
  status: string;
  customerId: string | null;
  subscriptionId: string | null;
};
