package org.acme.rateLimiting;

import io.quarkiverse.bucket4j.runtime.resolver.IdentityResolver;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Objects;

@ApplicationScoped
public class TenantIdResolver implements IdentityResolver {

    /*
    Per-Tenant Rate Limiting
        In a SaaS world, you don’t punish one tenant for another’s traffic.
        Let's isolate tenants using a custom header: X-Tenant-ID.
        We'll rate limit the /metrics/memory endpoint.
        Each tenant should get 3 requests every 20 seconds.
     */

    @Inject
    RoutingContext context;

    @Override
    public String getIdentityKey() {
        // extract the tenant ID from the custom header
        String tenantId = context.request().getHeader("X-Tenant-ID");

        // if the header is not present we can deny the request
        // or assign a deafult "anonymous" bucket. here we deny.
        if (Objects.isNull(tenantId) || tenantId.isBlank()) {
            // this will cause a 403 forbidden because no key is resolved
            return null;
        }
        return tenantId;
    }
}
