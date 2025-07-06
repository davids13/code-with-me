package org.acme.rateLimiting.ex2;

import jakarta.persistence.Column;

import java.time.LocalDateTime;
import java.util.List;

public class ApiKey {

    @Column(unique = true, nullable = false)
    public String keyValue;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String owner;

    @Column(nullable = false)
    public LocalDateTime createdAt;

    @Column
    public LocalDateTime lastUsed;

    @Column(nullable = false)
    public Boolean active = true;

    @Column(nullable = false)
    public Long usageCount = 0L;

    @Column
    public LocalDateTime expiresAt;

    @Column
    public String permissions; // JSON string for simplicity

    public static List<ApiKey> findActiveKeys() {
        return List.of(new ApiKey());
    }
}
