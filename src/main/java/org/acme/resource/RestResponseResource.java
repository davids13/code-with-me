package org.acme.resource;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.acme.error.ErrorResponse;
import org.acme.restResponses.Item;

import java.net.URI;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Path("items")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
// Specifies that methods in this class will, by default, produce JSON responses. quarkus-rest-jackson handles the conversion.
public class RestResponseResource {

    // https://myfear.substack.com/p/quarkus-http-response-guide-java-developers?utm_source=post-email-title&publication_id=4194688&post_id=163605881&utm_campaign=email-post-title&isFreemail=true&r=1om7hi&triedRedirect=true&utm_medium=email

    // Serving A basic Response with Response.ok()

    private static final Map<String, Item> items = new ConcurrentHashMap<>();

    static {
        items.put("1", new Item("1", "Sample Item", "This is a sample item."));
        items.put("2", new Item("2", "Another Item", "This is another item."));
    }

    @GET
    @Path("simple/{id}")
    public Response getItem(@PathParam("id") String id) {
        Item item = items.get(id);
        return (item != null) ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("simple-list")
    public Response getAllItems() {
        return Response.ok(new ArrayList<>(items.values())).build();
    }

    @GET
    @Path("direct/{id}")
    public Item getDirect(@PathParam("id") String id) {
        Item item = items.get(id);
        if (item == null) {
            throw new NotFoundException("Item with id " + id + " not found.");
        }
        return item;
    }

    @POST
    public Response createItem(Item item, @Context UriInfo uriInfo) {
        // Simple ID generation, for example
        //item.id() = UUID.randomUUID().toString();
        items.put(item.id(), item);

        // Build URI for the newly created resource for the Location header
        URI location = uriInfo.getAbsolutePathBuilder().path(item.id()).build();

        // 201 Created with location header and the created item in the body
        return Response.created(location).entity(item).build();
    }

    // Want to return an empty body? Useful for operations that succeed but don't need to return data (e.g., some DELETE operations). Send a 204 No Content
    @GET
    @Path("empty")
    public Response getEmpty() {
        return Response.noContent().build();
    }

    @GET
    @Path("notfoundexample/{id}")
    public Response getItemByIdWithManualNotFound(@PathParam("id") String id) {
        Item item = items.get(id);
        if (item != null) {
            return Response.ok(item).build();
        } else {
            // Explicitly return 404 Not Found
            // The entity here is a simple String, but it will be wrapped as JSON
            // because of the class-level @Produces. For a plain text error,
            // you'd override .type(MediaType.TEXT_PLAIN).
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Item with id " + id + " not found.\"}")
                    // .type(MediaType.APPLICATION_JSON) // Redundant if class @Produces is JSON and
                    // entity is complex enough or a String that looks like JSON
                    .build();
        }
    }

    // Adding Custom Headers and Cookies
    @GET
    @Path("with-headers")
    public Response getItemWithCustomHeaders() {
        Item item = new Item("id789", "Header Item", "Item with custom headers.");
        return Response.ok(item)
                .header("X-Custom-Header", "MyValue123") // Adds a header to the response.
                .header("X-Another-Header", "AnotherCollValue")
                .cookie(new NewCookie.Builder("session-id") // setting a cookie
                        .value("abcxyz789")
                        .path("items")
                        .domain("localhost") // Be careful with domain in real apps
                        .maxAge(36000)
                        .secure(false) // true in production over HTTPS
                        .httpOnly(true)
                        .build())
                .build();
    }

    // Structuring Error Responses with POJOs
    @GET
    @Path("{id}")
    public Response getItemById(@PathParam("id") String id) {
        Item item = items.get(id);

        if (Objects.nonNull(item)) {
            return Response.ok(item).build();
        } else {
            ErrorResponse errorResponse = new ErrorResponse("E404_ITEM_NOT_FOUND",
                    "Item with id " + id + " not found.");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(errorResponse) // Jackson will serialize this to JSON
                    .build();
        }
    }
}
