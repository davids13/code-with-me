package org.acme.rateLimiting.ex2;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Provider
public class RateLimitingInterceptor implements ContainerRequestFilter {

    // https://myfear.substack.com/p/quarkus-api-security-token?utm_source=post-email-title&publication_id=4194688&post_id=166504594&utm_campaign=email-post-title&isFreemail=true&r=1om7hi&triedRedirect=true&utm_medium=email
    // Rate Limiting: One Key, One Minute, One Hundred Queries

    private static final int MAX_REQUESTS_PER_MINUTE = 100;
    private final ConcurrentHashMap<String, AtomicInteger> requestsCounts = new ConcurrentHashMap<>();

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        final String apikey = containerRequestContext.getHeaderString("X-API-KEY");

        if (apikey != null) {
            AtomicInteger count = requestsCounts.computeIfAbsent(apikey, k -> new AtomicInteger(0));

            if (count.incrementAndGet() > MAX_REQUESTS_PER_MINUTE) {
                containerRequestContext.abortWith(
                        Response.status(Response.Status.TOO_MANY_REQUESTS)
                                .entity(new ApiKeyResource.ErrorResponse("Rate limit exceeded, Agent!"))
                                .build()
                );
            }
        }
    }
}
