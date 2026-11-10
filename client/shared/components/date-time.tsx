import { useFormatter } from "next-intl";

/** Accepts ISO strings and the API's "yyyy-MM-dd HH:mm:ss" UTC format. */
const toDate = (value: string) =>
  new Date(/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/.test(value) ? `${value.replace(" ", "T")}Z` : value);

export function DateTime({ value }: { value: string }) {
  const format = useFormatter();
  return <>{format.dateTime(toDate(value), { dateStyle: "short", timeStyle: "short" })}</>;
}
