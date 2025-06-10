package org.acme.graphqll;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;

@GraphQLApi
public class UserMutation {

    @Mutation("updateUserEmail")
    public User updateUserEmail(@Name("id") final int id, @Name("email") final String email) {
        return updateEmailInService(id, email);
    }

    private User updateEmailInService(final int id, final String email) {
        return new User(id, "Bob", email);
    }
}
