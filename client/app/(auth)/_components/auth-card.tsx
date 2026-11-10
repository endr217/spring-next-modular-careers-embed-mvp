import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/shared/components/ui/card";

type Props = { title: string; description: string; children: React.ReactNode };

export function AuthCard({ title, description, children }: Props) {
  return (
    <main className="flex flex-1 items-center justify-center p-4">
      <Card className="w-full max-w-sm">
        <CardHeader>
          <CardTitle>{title}</CardTitle>
          <CardDescription>{description}</CardDescription>
        </CardHeader>
        <CardContent>{children}</CardContent>
      </Card>
    </main>
  );
}
