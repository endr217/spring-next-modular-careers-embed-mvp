import { Table, TableBody, TableHead, TableHeader, TableRow } from "@/shared/components/ui/table";
import { cn } from "@/shared/lib/utils";

type Props = { head: string[]; children: React.ReactNode };

export function DataTable({ head, children }: Props) {
  return (
    <div className="rounded-xl border bg-card shadow-sm">
      <Table>
        <TableHeader>
          <TableRow>
            {head.map((title, index) => (
              <TableHead key={title || index} className={cn("px-4", !title && "w-px")}>
                {title}
              </TableHead>
            ))}
          </TableRow>
        </TableHeader>
        <TableBody>{children}</TableBody>
      </Table>
    </div>
  );
}
