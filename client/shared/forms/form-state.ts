export type FormState = {
  message?: string;
  success?: string;
  errors?: Record<string, string>;
  values?: Record<string, string>;
};

export const EMPTY_FORM_STATE: FormState = {};
