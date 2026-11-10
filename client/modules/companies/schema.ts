import { z } from "zod";

export const onboardingSchema = z.object({
  company_name: z.string().trim().min(1, "companyNameRequired").max(255, "tooLong"),
  company_slug: z
    .string()
    .trim()
    .min(1, "companySlugRequired")
    .max(255, "tooLong")
    .regex(/^[a-z0-9-]+$/, "companySlugPattern"),
  company_email: z.string().trim().min(1, "emailRequired").pipe(z.email("emailInvalid")),
});
