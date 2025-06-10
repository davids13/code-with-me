package org.acme.jwt;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;

@ApplicationScoped
public class TokenService {

    // https://myfear.substack.com/p/jwt-quarkus-murder-mystery

    public String generateToken(final String username, String... roles) {
        return Jwt.issuer("https://quarkus.io/jwt-case")
                .upn(username)
                .groups(new HashSet<>(Arrays.asList(roles)))
                .expiresIn(Duration.ofHours(1))
                .sign();
    }
}
