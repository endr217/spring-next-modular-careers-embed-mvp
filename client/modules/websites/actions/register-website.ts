"use server";

import { redirect } from "next/navigation";
import { requireCompany } from "@/modules/companies/server";
import { apiFetch } from "@/shared/api/api-fetch";
import type { FormState } from "@/shared/forms/form-state";
import { formValues } from "@/shared/forms/form-values";
import { parseForm } from "@/shared/forms/parse-form";
import { toFormState } from "@/shared/forms/to-form-state";
import { websiteSchema } from "../schema";
import type { Website } from "../types";

export async function registerWebsite(_: FormState, formData: FormData): Promise<FormState> {
  const values = formValues(formData);
  const parsed = await parseForm(websiteSchema, values);
  if (!parsed.success) return { errors: parsed.errors, values };

  let website: Website;
  try {
    const company = await requireCompany();
    website = await apiFetch<Website>("/websites/register", {
      method: "POST",
      body: { ...parsed.data, company_id: company.id },
    });
  } catch (error) {
    return toFormState(error, values);
  }
  redirect(`/websites/${website.id}`);
}
