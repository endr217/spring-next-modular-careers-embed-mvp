import { Field, FieldError, FieldLabel } from "@/shared/components/ui/field";
import { Input } from "@/shared/components/ui/input";

type Props = React.ComponentProps<typeof Input> & {
  name: string;
  label: string;
  error?: string;
};

export function TextField({ name, label, error, id = name, ...props }: Props) {
  return (
    <Field data-invalid={error ? true : undefined}>
      <FieldLabel htmlFor={id}>{label}</FieldLabel>
      <Input id={id} name={name} aria-invalid={error ? true : undefined} {...props} />
      <FieldError>{error}</FieldError>
    </Field>
  );
}
