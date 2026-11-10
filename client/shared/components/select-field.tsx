import { Field, FieldError, FieldLabel } from "@/shared/components/ui/field";
import { NativeSelect, NativeSelectOption } from "@/shared/components/ui/native-select";

type Option = { value: string | number; label: string };

type Props = React.ComponentProps<typeof NativeSelect> & {
  name: string;
  label: string;
  placeholder: string;
  options: Option[];
  error?: string;
};

export function SelectField({ name, label, placeholder, options, error, id = name, ...props }: Props) {
  return (
    <Field data-invalid={error ? true : undefined}>
      <FieldLabel htmlFor={id}>{label}</FieldLabel>
      <NativeSelect id={id} name={name} aria-invalid={error ? true : undefined} {...props}>
        <NativeSelectOption value="">{placeholder}</NativeSelectOption>
        {options.map((option) => (
          <NativeSelectOption key={option.value} value={option.value}>
            {option.label}
          </NativeSelectOption>
        ))}
      </NativeSelect>
      <FieldError>{error}</FieldError>
    </Field>
  );
}
