package org.acme.rateLimiting.ex2;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ApiKeyService {

    public List<ApiKey> getAllActiveKeys() {
        return ApiKey.findActiveKeys();
    }

    @Transactional
    public void deactivateExpiredKeys() {
        //List<ApiKey> expiredKeys = ApiKey.findExpiredKeys();
        List<ApiKey> expiredKeys = List.of(new ApiKey());
        expiredKeys.forEach(key -> {
            key.active = false;
            //key.persist();
        });
    }
}
