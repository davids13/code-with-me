package org.acme.secure;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@ApplicationScoped
@Path("secured-api")
public class SecureResource {

    // https://myfear.substack.com/p/secure-authentication-quarkus-jpa

    @GET
    @Path("public") // You can still have unauthenticated endpoints like /public
    public String publicEndpoint() {
        return "This is public.";
    }

    @GET
    @Path("user")
    @RolesAllowed("user") // Endpoints annotated with @RolesAllowed are protected // Roles must match those defined in the database role field.
    public String userEndpoint() {
        return "Hello, user";
    }

    @GET
    @Path("admin")
    @RolesAllowed("admin") // Endpoints annotated with @RolesAllowed are protected // Roles must match those defined in the database role field.
    public String adminEndpoint() {
        return "Hello, admin";
    }

}
