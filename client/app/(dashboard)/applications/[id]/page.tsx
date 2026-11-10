import { Suspense } from "react";
import { ApplicationDetails } from "@/modules/applications/components/application-details";
import { LoadingText } from "@/shared/components/loading-text";

async function Details({ params }: Pick<PageProps<"/applications/[id]">, "params">) {
  const { id } = await params;
  return <ApplicationDetails id={id} />;
}

export default function ApplicationPage({ params }: PageProps<"/applications/[id]">) {
  return (
    <Suspense fallback={<LoadingText />}>
      <Details params={params} />
    </Suspense>
  );
}
