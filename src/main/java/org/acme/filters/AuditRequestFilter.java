package org.acme.filters;

import jakarta.annotation.Priority;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
@Audited
@Priority(100) // @Priority if you care about the order in which filters run. - Lower = runs earlier
public class AuditRequestFilter implements ContainerRequestFilter {

    private static final Logger LOG = Logger.getLogger(AuditRequestFilter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        LOG.infof("[AUDIT] Performing detailed audit check for: %s %s", containerRequestContext.getMethod(), containerRequestContext.getUriInfo().getAbsolutePath());
    }
}
