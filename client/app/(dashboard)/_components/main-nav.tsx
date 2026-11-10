"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { useTranslations } from "next-intl";
import { cn } from "@/shared/lib/utils";

const LINKS = [
  { href: "/websites", label: "websites" },
  { href: "/company/subscription", label: "subscription" },
] as const;

export function MainNav() {
  const pathname = usePathname();
  const t = useTranslations("nav");

  return (
    <nav className="flex gap-1">
      {LINKS.map((link) => {
        const active = pathname.startsWith(link.href);
        return (
          <Link
            key={link.href}
            href={link.href}
            aria-current={active ? "page" : undefined}
            className={cn(
              "rounded-lg px-3 py-2 text-sm font-medium text-muted-foreground hover:text-foreground",
              active && "bg-muted text-foreground",
            )}
          >
            {t(link.label)}
          </Link>
        );
      })}
    </nav>
  );
}
