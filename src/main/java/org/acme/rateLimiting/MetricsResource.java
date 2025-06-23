package org.acme.rateLimiting;

import io.quarkiverse.bucket4j.runtime.RateLimited;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("api/v1")
@Produces(MediaType.TEXT_PLAIN)
public class MetricsResource {

    // https://myfear.substack.com/p/smart-api-rate-limiting-quarkus-bucket4j?utm_source=post-email-title&publication_id=4194688&post_id=165452712&utm_campaign=email-post-title&isFreemail=true&r=1om7hi&triedRedirect=true&utm_medium=email

    @Inject
    TenantService tenantService;
    @Inject
    TenantContext tenantContext;

    /**
     * Let's apply a simple, global rate limit to it:
     * no more than 5 requests every 10 seconds for everyone combined.
     */
    @GET
    @Path("metrics/cpu")
    @RateLimited(bucket = "cpu-metrics-limit")
    public String getCpuMetrics() {
        return "CPU usage: 42%";
    }

    @GET
    @Path("metrics/memory")
    @RateLimited(bucket = "memory-metrics-limit", identityResolver = TenantIdResolver.class)
    public String getMemoryMetrics() {
        return "Memory usage: 58%";
    }

    @GET
    @Path("reports/generate")
    @DynamicRateLimited
    public String generateReport() {
        // this is a more "expensive" operation
        return "Report successfully generated for " + tenantContext.getTenantId();
    }

}
