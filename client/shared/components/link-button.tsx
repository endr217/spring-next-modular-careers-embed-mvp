import Link from "next/link";
import { Button } from "@/shared/components/ui/button";

type Props = Omit<React.ComponentProps<typeof Button>, "asChild"> & { href: string };

export function LinkButton({ href, children, ...props }: Props) {
  return (
    <Button asChild {...props}>
      <Link href={href}>{children}</Link>
    </Button>
  );
}
