"use client";

import { useActionState } from "react";
import { SubmitButton } from "@/shared/components/submit-button";
import { EMPTY_FORM_STATE, type FormState } from "@/shared/forms/form-state";

type Props = Omit<React.ComponentProps<typeof SubmitButton>, "pending" | "action"> & {
  action: (prev: FormState) => Promise<FormState>;
  confirmText?: string;
};

export function ActionButton({ action, confirmText, ...props }: Props) {
  const [state, formAction, pending] = useActionState(action, EMPTY_FORM_STATE);

  const confirm = (event: React.FormEvent) => {
    if (confirmText && !window.confirm(confirmText)) event.preventDefault();
  };

  return (
    <form action={formAction} onSubmit={confirm} className="flex flex-col items-end gap-1">
      <SubmitButton pending={pending} {...props} />
      {state.message && (
        <span role="alert" className="w-48 text-right text-xs whitespace-normal text-destructive">
          {state.message}
        </span>
      )}
    </form>
  );
}
