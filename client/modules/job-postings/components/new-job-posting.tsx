import { createJobPosting } from "../actions/create-job-posting";
import { JobPostingForm } from "./job-posting-form";

export function NewJobPosting({ websiteId }: { websiteId: number }) {
  return <JobPostingForm action={createJobPosting.bind(null, websiteId)} websiteId={websiteId} />;
}
