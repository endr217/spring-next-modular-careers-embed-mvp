export type CompanyRole = "owner" | "admin" | "viewer";

export type ProfileCompany = {
  id: number;
  name: string;
  slug: string;
  logo_url: string | null;
  user_role: CompanyRole;
  is_primary: boolean;
};

export type Profile = {
  user: { id: number; name: string; email: string; created_at: string };
  has_companies: boolean;
  companies_count: number;
  companies: ProfileCompany[];
  primary_company: ProfileCompany | null;
};
