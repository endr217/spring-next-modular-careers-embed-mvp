export class ApiError extends Error {
  constructor(
    readonly status: number,
    message: string,
    readonly errors: Record<string, string> = {},
  ) {
    super(message);
  }
}
