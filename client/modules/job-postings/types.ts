export type JobType = "full-time" | "part-time" | "freelance";

export type JobPosting = {
  id: number;
  title: string;
  location: string;
  job_type: JobType;
  salary_from: number | null;
  salary_to: number | null;
  description: string;
  is_active: boolean;
  slug: string;
  published_at: string;
  expires_at: string | null;
  website: { id: number; name: string; domain: string };
  company: { id: number; name: string; slug: string };
};
