import { ESLint } from "eslint";
import { describe, expect, it } from "vitest";

const eslint = new ESLint();
const RULE = "no-restricted-imports";

async function violations(filePath: string, code: string): Promise<string[]> {
  const [result] = await eslint.lintText(code, { filePath });
  return result.messages.map((message) => message.ruleId ?? "parse-error");
}

describe("architecture rules enforced by ESLint", () => {
  it("forbids application code from importing tests", async () => {
    expect(await violations("app/page.tsx", 'import "@/tests/helpers";\n')).toContain(RULE);
    expect(await violations("modules/websites/queries/get-websites.ts", 'import "@/tests/x";\n')).toContain(RULE);
  });

  it("forbids importing another module's internal files", async () => {
    expect(
      await violations("app/(dashboard)/websites/page.tsx", 'import "@/modules/websites/queries/get-websites";\n'),
    ).toContain(RULE);
    expect(await violations("modules/billing/server.ts", 'import "../auth/server";\n')).toContain(RULE);
    expect(
      await violations("modules/websites/queries/get-websites.ts", 'import "../../companies/server";\n'),
    ).toContain(RULE);
  });

  it("allows a layer file to import another layer of its own module", async () => {
    expect(
      await violations("modules/job-postings/actions/create-job-posting.ts", 'import "../schema";\nimport "./save-job-posting";\n'),
    ).not.toContain(RULE);
  });

  it("allows public module entries, components and the shared kernel", async () => {
    const allowed = [
      'import "@/modules/companies";',
      'import "@/modules/companies/server";',
      'import "@/modules/websites/components/websites-list";',
      'import "@/shared/api/api-fetch";',
    ].join("\n");
    expect(await violations("app/(dashboard)/websites/page.tsx", allowed)).not.toContain(RULE);
  });

  it("forbids frameworks and I/O in a module's domain folder", async () => {
    const file = "modules/embed/domain/load-strategy.ts";
    expect(await violations(file, 'import "next/navigation";\n')).toContain(RULE);
    expect(await violations(file, 'import "react";\n')).toContain(RULE);
    expect(await violations(file, 'import "@/shared/api/api-fetch";\n')).toContain(RULE);
    expect(await violations(file, 'import "zod";\n')).not.toContain(RULE);
  });

  it("forbids the shared kernel from importing modules", async () => {
    expect(await violations("shared/components/page-header.tsx", 'import "@/modules/auth";\n')).toContain(RULE);
  });
});
