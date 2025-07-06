package org.acme.schedule;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.rateLimiting.ex2.ApiKeyService;
import org.jboss.logging.Logger;

@ApplicationScoped
public class MaintenanceScheduler {

    private static final Logger LOG = Logger.getLogger(MaintenanceScheduler.class);

    ApiKeyService apiKeyService;

    @Inject
    public MaintenanceScheduler(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    /*
        To clean up expired keys hourly and log a daily usage report.
        Use Quarkus Scheduler with annotations like @Scheduled(every = "1h")
        and @Scheduled(cron = "0 0 9 * * ?")
    */
    //@Scheduled(every = "1h") // every hour
    @Scheduled(cron = "0 0 9 * * ?") // Daily at 9 AM
    void cleanupExpiredKeys() {
        LOG.info("🧹 Starting expired key cleanup mission...");
        apiKeyService.deactivateExpiredKeys();
        LOG.info("Expired key cleanup completed!");
    }
}
