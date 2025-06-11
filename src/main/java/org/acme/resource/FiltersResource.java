package org.acme.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.filters.Audited;
import org.jboss.logging.Logger;

@ApplicationScoped
@Path("filters")
@Produces(MediaType.TEXT_PLAIN)
public class FiltersResource {

    // https://myfear.substack.com/p/http-interceptors-quarkus-jaxrs-filters?utm_source=post-email-title&publication_id=4194688&post_id=162872463&utm_campaign=email-post-title&isFreemail=true&r=1om7hi&triedRedirect=true&utm_medium=email

    /*
    What You’ll Build
        We’re going to:
            Log all incoming HTTP requests and outgoing responses.
            Add custom headers to all responses.
            Create an auditing filter that only runs on endpoints we mark with a special annotation.
            Learn how Quarkus handles standard JAX-RS features like @Provider, @NameBinding, and @Priority.
    */

    private static final Logger LOG = Logger.getLogger(FiltersResource.class);

    // Every response includes X-Global-Trace-ID.

    /**
     * only global filters run
     *
     * @return String
     */
    @GET
    public String hello() {
        LOG.info("Executing standard hello() method.");
        return "Hello from Quarkus";
    }

    /**
     * global filters + audit filter will display
     *
     * @return String
     */
    @GET
    @Path("audited")
    @Audited
    public String helloAudited() {
        LOG.info("Executing AUDITED helloAudited() method.");
        return "Hello from the *audited* endpoint!";
    }
}
