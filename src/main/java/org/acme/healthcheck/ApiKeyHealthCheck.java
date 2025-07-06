package org.acme.healthcheck;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.rateLimiting.ex2.ApiKeyService;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class ApiKeyHealthCheck implements HealthCheck {

    ApiKeyService apiKeyService;

    @Inject
    public ApiKeyHealthCheck(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @Override
    public HealthCheckResponse call() {
        try {
            int activeKeys = apiKeyService.getAllActiveKeys().size();
            return HealthCheckResponse.named("API Key Service")
                    .up()
                    .withData("activeKeys", activeKeys)
                    .build();
        } catch (Exception e) {
            return HealthCheckResponse.named("API KEY Service")
                    .down()
                    .withData("error", e.getMessage())
                    .build();
        }
    }
}
