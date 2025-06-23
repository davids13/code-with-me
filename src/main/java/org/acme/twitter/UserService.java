package org.acme.twitter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService {

    @Transactional
    public TwitterUser createUser(String username, String displayName, String bio) {
        var user = new TwitterUser();
        user.username = username;
        user.displayName = displayName;
        user.bio = bio;
        user.persist();
        return user;
    }

    public TwitterUser findByUsername(String username) {
        return TwitterUser.findByUsername(username);
    }

    @Transactional
    public TwitterUser getOrCreateUser(String username) {
        var user = findByUsername(username);
        return user != null ? user : createUser(username, username, "New Chirper user!");
    }
}
