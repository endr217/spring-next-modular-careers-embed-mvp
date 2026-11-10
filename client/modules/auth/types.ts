export type AuthUser = {
  id: number;
  name: string;
  surname: string;
  email: string;
  created_at: string;
};

export type AuthResponse = { user: AuthUser; token: string };

export type PasswordResetResponse = { message: string; success: boolean; status: string };
