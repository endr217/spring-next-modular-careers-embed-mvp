import { useTranslations } from "next-intl";
import { LinkButton } from "@/shared/components/link-button";
import { SubmitButton } from "@/shared/components/submit-button";

type Props = { pending: boolean; cancelHref: string; submitLabel?: string };

export function FormActions({ pending, cancelHref, submitLabel }: Props) {
  const t = useTranslations("common");

  return (
    <div className="flex justify-end gap-2">
      <LinkButton href={cancelHref} variant="outline">
        {t("cancel")}
      </LinkButton>
      <SubmitButton pending={pending}>{submitLabel ?? t("save")}</SubmitButton>
    </div>
  );
}
