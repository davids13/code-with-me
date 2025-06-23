package org.acme.rateLimiting;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class TenantFilter implements ContainerRequestFilter {

    private static final Logger LOG = Logger.getLogger(TenantFilter.class);

    @Inject
    TenantContext tenantContext;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        tenantContext.setTenantId(containerRequestContext.getHeaderString("X-Tenant-ID"));
        LOG.infof("X-Tenant-ID " + tenantContext.getTenantId());
    }
}
