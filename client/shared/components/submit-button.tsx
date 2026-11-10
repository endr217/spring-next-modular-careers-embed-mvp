import { Button } from "@/shared/components/ui/button";

type Props = React.ComponentProps<typeof Button> & { pending: boolean };

export function SubmitButton({ pending, disabled, ...props }: Props) {
  return <Button type="submit" disabled={pending || disabled} aria-busy={pending} {...props} />;
}
