import { z } from "zod";

export const JOB_TYPES = ["full-time", "part-time", "freelance"] as const;

const salary = z
  .string()
  .trim()
  .regex(/^\d*$/, "salaryInvalid")
  .transform((value) => (value ? Number(value) : null));

export const jobPostingSchema = z
  .object({
    title: z.string().trim().min(1, "titleRequired").max(255, "tooLong"),
    location: z.string().trim().min(1, "locationRequired").max(255, "tooLong"),
    job_type: z.enum(JOB_TYPES, "jobTypeInvalid"),
    salary_from: salary,
    salary_to: salary,
    description: z.string().trim().min(1, "descriptionRequired"),
    expires_at: z
      .string()
      .trim()
      .regex(/^$|^\d{4}-\d{2}-\d{2}$/, "dateInvalid")
      .transform((value) => value || null),
    is_active: z
      .string()
      .optional()
      .transform((value) => value === "on"),
  })
  .refine((value) => value.salary_from === null || value.salary_to === null || value.salary_to >= value.salary_from, {
    message: "salaryRange",
    path: ["salary_to"],
  });
