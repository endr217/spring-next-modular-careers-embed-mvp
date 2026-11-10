export type ApplicationStatus = "new" | "reviewed" | "accepted" | "rejected";

export type Application = {
  id: number;
  job_posting_id: number;
  name: string;
  email: string;
  motivation_text: string | null;
  cv_file_path: string;
  status: ApplicationStatus;
  submitted_at: string;
  job_posting: { id: number; title: string; slug: string; company_name: string; company_email: string };
};
