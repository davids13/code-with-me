package org.acme.resource;

import org.acme.graphqll.User;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

@GraphQLApi
public class UserResource {

    // https://myfear.substack.com/p/graphql-quarkus-java-api-guide

    @Query("user")
    @Description("Fetch a user UD including email")
    public User getUser(@Name("id") final int id) {
        return fetchUserWithDetail(id);
    }

    private User fetchUserWithDetail(final int id) {
        return new User(id, "Bob", "bob@email.com");
    }
}
