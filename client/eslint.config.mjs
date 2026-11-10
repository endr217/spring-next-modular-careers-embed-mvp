import { defineConfig, globalIgnores } from "eslint/config";
import nextVitals from "eslint-config-next/core-web-vitals";
import nextTs from "eslint-config-next/typescript";

// A module is used from outside only through index.ts (client-safe types and schemas),
// server.ts (server-only queries) and its components/ files. Inside a module, files
// reach their own layers with relative paths.
const MODULE_BOUNDARY = {
  group: ["@/modules/*/*", "!@/modules/*/server", "!@/modules/*/components", "!@/modules/*/components/**"],
  message:
    "Do not import a module's internal files. Use @/modules/<module>, @/modules/<module>/server or its components/.",
};

// shared/ is the common kernel: it knows nothing about modules, so there are no cycles.
const SHARED_KERNEL = {
  group: ["@/modules/*", "@/modules/**"],
  message: "shared/ must not import from modules/.",
};

// Deleting tests/ must leave the application compiling.
const NO_TESTS = {
  group: ["@/tests/*", "**/tests/*"],
  message: "Application code must not import from tests/.",
};

// A relative path must not leave its module. How many levels up are allowed depends
// on whether the file sits in the module root or inside one of its layer folders.
const escapeMessage = "Reach another module through @/modules/<module>, not a relative path.";
const NO_ESCAPE_FROM_ROOT = { group: ["../*/*", "../../**"], message: escapeMessage };
const NO_ESCAPE_FROM_LAYER = { group: ["../../**"], message: escapeMessage };

// Pure domain logic stays free of frameworks so it can be reused and reasoned about.
const DOMAIN_PURITY = [
  { group: ["next", "next/*"], message: "Domain logic must not import Next.js." },
  {
    group: ["react", "react/*", "react-dom", "react-dom/*"],
    message: "Domain logic must not import React.",
  },
  { group: ["@/shared/api/*", "@/shared/auth/*"], message: "Domain logic must not do I/O." },
];

const restrict = (...patterns) => ({
  "no-restricted-imports": ["error", { patterns }],
});

// A later block replaces an earlier block's rule, so every block lists all of its restrictions.
const eslintConfig = defineConfig([
  ...nextVitals,
  ...nextTs,
  globalIgnores([".next/**", "out/**", "build/**", "next-env.d.ts"]),
  {
    files: ["app/**/*.{ts,tsx}", "modules/*/components/**/*.{ts,tsx}", "i18n/**/*.ts", "proxy.ts"],
    rules: restrict(MODULE_BOUNDARY, NO_TESTS),
  },
  {
    files: ["shared/**/*.{ts,tsx}"],
    rules: restrict(SHARED_KERNEL, NO_TESTS),
  },
  {
    files: ["app/**/*.tsx", "modules/**/*.tsx", "shared/**/*.tsx"],
    rules: {
      "react/jsx-no-literals": ["error", { noStrings: true, ignoreProps: true }],
    },
  },
  {
    files: ["modules/*/*.{ts,tsx}"],
    rules: restrict(MODULE_BOUNDARY, NO_ESCAPE_FROM_ROOT, NO_TESTS),
  },
  {
    files: ["modules/*/*/**/*.{ts,tsx}"],
    ignores: ["modules/*/components/**", "modules/*/domain/**"],
    rules: restrict(MODULE_BOUNDARY, NO_ESCAPE_FROM_LAYER, NO_TESTS),
  },
  {
    files: ["modules/*/domain/**/*.ts"],
    rules: restrict(MODULE_BOUNDARY, NO_ESCAPE_FROM_LAYER, NO_TESTS, ...DOMAIN_PURITY),
  },
]);

export default eslintConfig;
