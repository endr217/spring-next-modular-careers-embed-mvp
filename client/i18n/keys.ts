import messages from "../messages/en.json";

export type ValidationKey = keyof typeof messages.validation;

export function validationKey(key: string | undefined): ValidationKey {
  return key && Object.hasOwn(messages.validation, key) ? (key as ValidationKey) : "invalid";
}
