import { useTranslations } from "next-intl";
import { Badge } from "@/shared/components/ui/badge";
import type { ApplicationStatus } from "../types";

const VARIANTS = {
  new: "default",
  reviewed: "secondary",
  accepted: "outline",
  rejected: "destructive",
} as const;

export function StatusBadge({ status }: { status: ApplicationStatus }) {
  const t = useTranslations("applications.statuses");
  return <Badge variant={VARIANTS[status]}>{t(status)}</Badge>;
}
