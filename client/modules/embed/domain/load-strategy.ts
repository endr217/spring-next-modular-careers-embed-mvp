export const LOAD_STRATEGIES = ["defer", "async", "none"] as const;

export type LoadStrategy = (typeof LOAD_STRATEGIES)[number];

export function toLoadStrategy(value: string): LoadStrategy {
  return (LOAD_STRATEGIES as readonly string[]).includes(value) ? (value as LoadStrategy) : "defer";
}
