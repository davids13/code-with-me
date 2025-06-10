package org.acme.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.error.MyCustomApplicationException;

@ApplicationScoped
@Path("")
public class ErrorResource {

    // https://myfear.substack.com/p/quarkus-custom-error-pages-rest-qute

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus";
    }

    @GET
    @Path("/error")
    public String cause500() {
        throw new RuntimeException("Simulated failure!");
    }

    @GET
    @Path("/api/error")
    @Produces(MediaType.APPLICATION_JSON)
    public String apiError() {
        throw new RuntimeException("Simulated API error!");
    }

    @GET
    @Path("custom-error")
    @Produces(MediaType.APPLICATION_JSON)
    public String triggerCustomError() {
        throw new MyCustomApplicationException("This is a test of the custom application exception.");
    }
}
