/** Paginated list in the API's length-aware paginator format. */
export type Page<T> = {
  current_page: number;
  data: T[];
  last_page: number;
  per_page: number;
  total: number;
};
