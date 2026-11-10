import { Field, FieldError, FieldLabel } from "@/shared/components/ui/field";
import { Textarea } from "@/shared/components/ui/textarea";

type Props = React.ComponentProps<typeof Textarea> & {
  name: string;
  label: string;
  error?: string;
};

export function TextareaField({ name, label, error, id = name, ...props }: Props) {
  return (
    <Field data-invalid={error ? true : undefined}>
      <FieldLabel htmlFor={id}>{label}</FieldLabel>
      <Textarea id={id} name={name} aria-invalid={error ? true : undefined} {...props} />
      <FieldError>{error}</FieldError>
    </Field>
  );
}
