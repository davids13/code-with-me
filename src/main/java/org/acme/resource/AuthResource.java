package org.acme.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.jwt.TokenService;

import java.util.Objects;

@ApplicationScoped
@Path("auth")
public class AuthResource {

    TokenService tokenService;

    @Inject
    public AuthResource(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GET
    @Path("login")
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(@QueryParam("username") final String user, @QueryParam("role") final String role) {
        if (Objects.isNull(user) || Objects.isNull(role)) {
            return Response.status(Response.Status.BAD_GATEWAY).entity("Missing params").build();
        }
        return Response.status(Response.Status.ACCEPTED).entity(tokenService.generateToken(user, role)).build();
    }
}
