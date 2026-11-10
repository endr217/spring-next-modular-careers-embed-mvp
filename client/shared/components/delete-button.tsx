"use client";

import { useTranslations } from "next-intl";
import { ActionButton } from "@/shared/components/action-button";
import type { FormState } from "@/shared/forms/form-state";

type Props = {
  action: (prev: FormState) => Promise<FormState>;
  confirmText: string;
};

export function DeleteButton({ action, confirmText }: Props) {
  const t = useTranslations("common");
  return (
    <ActionButton action={action} confirmText={confirmText} variant="ghost" size="sm" className="text-destructive">
      {t("delete")}
    </ActionButton>
  );
}
