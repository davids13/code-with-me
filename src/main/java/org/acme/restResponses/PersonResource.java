package org.acme.restResponses;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.flyway.Person;

import java.util.List;

@ApplicationScoped
@Path("people")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {

    // https://myfear.substack.com/p/quarkus-flyway-database-migrations-java?utm_source=post-email-title&publication_id=4194688&post_id=162897978&utm_campaign=email-post-title&isFreemail=true&r=1om7hi&triedRedirect=true&utm_medium=email

    @POST
    @Transactional
    public Response create(final Person person) {
        person.persist();
        return Response.status(Response.Status.CREATED).entity(person).build();
    }

    @GET
    public List<Person> list() {
        return Person.listAll();
    }
}
