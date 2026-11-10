import { Card, CardContent, CardHeader, CardTitle } from "@/shared/components/ui/card";

export function FormCard({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <Card className="mx-auto max-w-2xl">
      <CardHeader>
        <CardTitle>{title}</CardTitle>
      </CardHeader>
      <CardContent>{children}</CardContent>
    </Card>
  );
}
