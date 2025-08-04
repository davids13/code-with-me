package org.acme.healthcheck;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Liveness;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Liveness:
 * Reads Configuration:
 * - Gets a list of websites to monitor from application.yml under monitoring.sites
 * Makes HTTP Requests:
 * - Sends HTTP HEAD requests to each configured external site (like Google, GitHub, etc.)
 * Checks Response Codes:
 * - Determines if sites are "UP" (status < 400) or "DOWN" (status ≥ 400)
 * Handles Failures:
 * - Catches network errors, timeouts, and other exceptions as "DOWN" status
 */
@Liveness
@ApplicationScoped
public class ExternalSitesHealthCheck implements HealthCheck {

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10)).build();

    @ConfigProperties
    SitesConfig sitesConfig;

    //@Inject
  /*  public ExternalSitesHealthCheck(SitesConfig sitesConfig) {
        this.sitesConfig = sitesConfig;
    }*/

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("External Websites");
        boolean allSitesUp = true;

        for (String siteUrl : sitesConfig.sites) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(siteUrl))
                        .method("HEAD", HttpRequest.BodyPublishers.noBody())
                        .timeout(Duration.ofSeconds(10))
                        .build();

                HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

                if (response.statusCode() >= 400) {
                    responseBuilder.withData(siteUrl, "Down");
                    allSitesUp = false;
                } else {
                    responseBuilder.withData(siteUrl, "UP");
                }
            } catch (Exception e) {
                responseBuilder.withData(siteUrl, "DOWN - " + e.getMessage());
                allSitesUp = false;
            }
        }
        return responseBuilder.status(allSitesUp).build();
    }

}
