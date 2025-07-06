package org.acme.rateLimiting.ex2;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("api/keys")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiKeyResource {

    @Inject
    ApiKeyService apiKeyService;

    @GET
    @Path("stats")
    public Response getStats() {
        List<ApiKey> activeKeys = apiKeyService.getAllActiveKeys();
        long totalUsage = activeKeys.stream().mapToLong(key -> key.usageCount).sum();

        return Response.ok(new StatsResponse(
                activeKeys.size(),
                totalUsage,
                activeKeys.stream().mapToLong(key -> key.usageCount).max().orElse(0))).build();
    }

    public static class ErrorResponse {
        public String error;

        public ErrorResponse(String error) {
            this.error = error;
        }
    }

    public static class StatsResponse {
        public int totalActiveKeys;
        public long totalUsage;
        public long maxUsage;

        public StatsResponse(int totalActiveKeys, long totalUsage, long maxUsage) {
            this.totalActiveKeys = totalActiveKeys;
            this.totalUsage = totalUsage;
            this.maxUsage = maxUsage;
        }
    }
}
