import { updateJobPosting } from "../actions/update-job-posting";
import { getJobPosting } from "../queries/get-job-postings";
import { JobPostingForm } from "./job-posting-form";

export async function EditJobPosting({ id }: { id: string }) {
  const posting = await getJobPosting(id);
  const websiteId = posting.website.id;

  return (
    <JobPostingForm
      action={updateJobPosting.bind(null, posting.id, websiteId)}
      websiteId={websiteId}
      posting={posting}
    />
  );
}
