package org.acme.tenant;

import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Objects;

/**
 * TenantResolver hooks into SecurityIdentity to grab the logged-in user
 */
@ApplicationScoped
public class MyTenantResolver implements TenantResolver {
    @Inject
    SecurityIdentity securityIdentity;

    @Override
    public String getDefaultTenantId() {
        return "UNKNOWN_TENANT";
    }

    @Override
    public String resolveTenantId() {
        if (Objects.nonNull(securityIdentity) && !securityIdentity.isAnonymous()) {
            return securityIdentity.getPrincipal().getName();
        }
        return getDefaultTenantId();
    }
}
