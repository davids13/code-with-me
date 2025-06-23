package org.acme.rateLimiting;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class TenantService {

    private static final Map<String, String> TENANT_PLANS = Map.of(
            "tenant-free-user", "FREE",
            "tenant-pro-user", "PRO");

    public Optional<String> getPlanForTenant(String tenantId) {
        return Optional.ofNullable(TENANT_PLANS.get(tenantId));
    }
}
