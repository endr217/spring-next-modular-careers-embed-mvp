export function SuccessMessage({ message }: { message?: string }) {
  if (!message) return null;
  return (
    <p role="status" className="rounded-lg bg-emerald-500/10 px-3 py-2 text-sm text-emerald-700">
      {message}
    </p>
  );
}
