package org.acme.headers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.stream.Collectors;

@ApplicationScoped
@Path("header")
public class HeaderResource {

    /*
        In this code:
            We inject the HttpHeaders context to get access to the request headers.
            We read all the headers and return them in the response body.
            We add a custom header, X-Custom-Header, to our response.
    */

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response hello(@Context HttpHeaders headers) {
        String allHeaders = headers.getRequestHeaders().entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining("\n"));

        return Response.ok("Hello from RESTEasy Reactive!\n\n" + "Your request headers:\n" + allHeaders)
                .header("X-Custom-Header", "Hello from the server!").build();
    }

    @GET
    @Path("large-payload")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLargePayload() {
        String largeJson = "{\"data\":[" +
                           "{\"id\":1,\"name\":\"A very long name to make the payload larger\"},"
                                   .repeat(1000)
                           +
                           "{\"id\":1001,\"name\":\"Final entry\"}" +
                           "]}";
        return Response.ok(largeJson).build();
    }
}
