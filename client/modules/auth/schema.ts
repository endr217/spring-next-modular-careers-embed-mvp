import { z } from "zod";

const email = z.string().trim().min(1, "emailRequired").pipe(z.email("emailInvalid"));
const newPassword = z.string().min(8, "passwordMin").max(255, "tooLong");

export const loginSchema = z.object({
  email,
  password: z.string().min(1, "passwordRequired"),
});

export const registerSchema = z.object({
  name: z.string().trim().min(1, "nameRequired").max(255, "tooLong"),
  surname: z.string().trim().min(1, "surnameRequired").max(255, "tooLong"),
  email: email.transform((value) => value.toLowerCase()),
  password: newPassword,
});

export const forgotPasswordSchema = z.object({ email });

export const resetPasswordSchema = z
  .object({
    token: z.string().min(1, "tokenRequired"),
    email,
    password: newPassword,
    password_confirmation: z.string().min(1, "passwordConfirmationRequired"),
  })
  .refine((value) => value.password === value.password_confirmation, {
    message: "passwordConfirmed",
    path: ["password_confirmation"],
  });
