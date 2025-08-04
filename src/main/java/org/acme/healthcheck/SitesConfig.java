package org.acme.healthcheck;

import org.eclipse.microprofile.config.inject.ConfigProperties;

import java.util.List;

/**
 * The @ConfigProperties annotation tells Quarkus to map monitoring.sites into the sites field automatically
 */
@ConfigProperties(prefix = "monitoring")
public class SitesConfig {

    // https://www.the-main-thread.com/i/168759465/step-project-setup

    List<String> sites;

}
