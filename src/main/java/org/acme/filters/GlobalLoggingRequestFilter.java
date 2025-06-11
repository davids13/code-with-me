package org.acme.filters;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

/**
 * filter to log every incoming request
 * this global filter will apply to all endpoint
 * Global Request Filter: Log All the Things
 */
@Provider
public class GlobalLoggingRequestFilter implements ContainerRequestFilter {

    private static final Logger LOG = Logger.getLogger(GlobalLoggingRequestFilter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        LOG.infof("[GLOBAL] Received request: %s %s", containerRequestContext.getMethod(), containerRequestContext.getUriInfo().getAbsolutePath());
        containerRequestContext.setProperty("request-start-time", System.nanoTime());
    }
}
