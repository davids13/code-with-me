package org.acme.filters;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.UUID;

/**
 * filter to log every outcoming response
 * this global filter will apply to all endpoints as response
 * Global Response Filter: will add a custom header (X-Global-Trace-ID) to every response and measure how long the request took.
 */
@Provider
public class GlobalHeaderResponseFilter implements ContainerResponseFilter {

    private static final Logger LOG = Logger.getLogger(GlobalHeaderResponseFilter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) {
        containerResponseContext.getHeaders().add("X-Global-Trace-ID", UUID.randomUUID().toString().substring(0, 8));
        Object startTimeObj = containerRequestContext.getProperty("request-start-time");
        if (startTimeObj instanceof Long) {
            long durationNanos = System.nanoTime() - (Long) startTimeObj;
            LOG.infof("[GLOBAL] Request processed in %.2f ms", durationNanos / 1_000_000.0);
        }
        LOG.info("[GLOBAL] Response filter finished.");
    }
}
